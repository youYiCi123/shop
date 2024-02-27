package com.jxm.business.service.Impl;

import com.jxm.business.dto.ProcessDetailDto;
import com.jxm.business.dto.ProcessNodeConfig;
import com.jxm.business.dto.ProcessNodeUser;
import com.jxm.business.dto.UmsAdminConcat;
import com.jxm.business.mapper.ProcessConditionMapper;
import com.jxm.business.mapper.ProcessMapper;
import com.jxm.business.mapper.ProcessNodeMapper;
import com.jxm.business.mapper.ProcessNodeUserMapper;
import com.jxm.business.model.ProcessBriefDto;
import com.jxm.business.model.ProcessNodeParam;
import com.jxm.business.model.ProcessNodeUserParam;
import com.jxm.business.service.ProcessDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProcessDetailServiceImpl implements ProcessDetailService {

    @Autowired
    private ProcessMapper processMapper;

    @Autowired
    private ProcessNodeMapper processNodeMapper;

    @Autowired
    private ProcessNodeUserMapper processNodeUserMapper;

    @Autowired
    private ProcessConditionMapper processConditionMapper;

    @Override
    public ProcessDetailDto queryDetailContent(Long id) {
        ProcessDetailDto processDetailDto = new ProcessDetailDto();
        ProcessBriefDto processBriefDto = processMapper.getById(id);
        //获取流程名
        processDetailDto.setId(id);
        processDetailDto.setProcessName(processBriefDto.getProcessName());
        //获取流程详细信息
        ProcessNodeParam rootProcessNode = processNodeMapper.getByIdAndNull(id);
        ProcessNodeConfig processNodeConfig = new ProcessNodeConfig();
        BeanUtils.copyProperties(rootProcessNode,processNodeConfig);
        //子节点
        List<ProcessNodeParam> processNodeParams = processNodeMapper.getByIdAndNotNull(id);
        ProcessNodeConfig nodeConfig = montage(processNodeConfig, processNodeParams);
        processDetailDto.setNodeConfig(nodeConfig);
        return processDetailDto;
    }

    //拼接流程节点
    public ProcessNodeConfig montage(ProcessNodeConfig processNodeConfig,List<ProcessNodeParam> nodeParams){
        ProcessNodeConfig deepestChildNode = processNodeConfig.getDeepestChildNode();

        //父级是路由的话
        if(deepestChildNode.getType().equals(4)){
            List<ProcessNodeConfig> processNodeConfigs=new ArrayList<>();
            List<ProcessNodeParam> processNodeParams = nodeParams.stream().filter(t -> t.getType().equals(3) && t.getParentId().equals(deepestChildNode.getId())).collect(Collectors.toList());
            processNodeParams.stream().forEach(t->{
                ProcessNodeConfig childProcessNode = new ProcessNodeConfig();//根部
                BeanUtils.copyProperties(t,childProcessNode);
                childProcessNode.setNodeUserList(processNodeUserMapper.getListById(childProcessNode.getId()));
                childProcessNode.setConditionList(processConditionMapper.getListById(childProcessNode.getId()));
                childProcessNode.setConditionNodes(Arrays.asList());
                processNodeConfigs.add(childProcessNode);
            });
            deepestChildNode.setConditionNodes(processNodeConfigs);
            deepestChildNode.setConditionList(Arrays.asList());
        }else{
            deepestChildNode.setConditionNodes(Arrays.asList());
            deepestChildNode.setConditionList(Arrays.asList());
        }

        Optional<ProcessNodeParam> first = nodeParams.stream().filter(t -> t.getParentId().equals(deepestChildNode.getId())).findFirst();
        if (first.isPresent()) {
            ProcessNodeConfig childProcessNode = new ProcessNodeConfig();//根部
            BeanUtils.copyProperties(first.get(),childProcessNode);
            deepestChildNode.setChildNode(childProcessNode);
        }
        if(deepestChildNode.getChildNode()==null)
            return processNodeConfig;
        //操作人
        List<ProcessNodeUser> nodeUserParams = processNodeUserMapper.getListById(deepestChildNode.getId());
        deepestChildNode.setNodeUserList(nodeUserParams);
        montage(processNodeConfig,nodeParams);

        return processNodeConfig;
    }


    @Override
    public int updateDetailContent(ProcessDetailDto processDetailDto) {
        List<ProcessNodeParam> processNodeParams=new ArrayList<>();
        processNodeParams = findNextLayer(processDetailDto.getId(), null, processDetailDto.getNodeConfig(), processNodeParams);
        return processNodeMapper.batchInsert(processNodeParams);
    }

    /**
     * 流程processId,父级Id,ProcessNodeConfig，processNodeParams
     */
    public List<ProcessNodeParam> findNextLayer(Long processId,Long parentId,ProcessNodeConfig nodeConfig,List<ProcessNodeParam> processNodeParams){
        if(nodeConfig==null)
            return processNodeParams;
        if(nodeConfig.getConditionNodes().size()!=0){
            nodeConfig.getConditionNodes().stream().forEach(t->{
                ProcessNodeParam processNodeParam = new ProcessNodeParam();
                BeanUtils.copyProperties(t,processNodeParam);
                processNodeParam.setParentId(nodeConfig.getId());
                processNodeParam.setProcessId(processId);
                processNodeParams.add(processNodeParam);
            });
        }
        //插入用户信息
        if(nodeConfig.getNodeUserList().size()!=0){
            processNodeUserMapper.batchInsert(nodeConfig.getId(),nodeConfig.getNodeUserList());
        }
        //插入条件信息
        if(nodeConfig.getConditionList().size()!=0){
            processConditionMapper.batchInsert(nodeConfig.getId(),nodeConfig.getConditionList());
        }
        ProcessNodeParam processNodeParam = new ProcessNodeParam();
        BeanUtils.copyProperties(nodeConfig,processNodeParam);
        processNodeParam.setParentId(parentId);
        processNodeParam.setProcessId(processId);
        processNodeParams.add(processNodeParam);
        findNextLayer(processId,processNodeParam.getId(),nodeConfig.getChildNode(),processNodeParams);
        return Arrays.asList();
    }

}

