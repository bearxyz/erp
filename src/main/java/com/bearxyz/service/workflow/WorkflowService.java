package com.bearxyz.service.workflow;

import com.bearxyz.common.DataTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.*;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Created by bearxyz on 2017/8/8.
 */
@Transactional
@Service
public class WorkflowService {

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ManagementService managementService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private HistoryService historyService;

    public DataTable<Model> getAllModelList(){
        DataTable<Model> dt = new DataTable<>();
        List<Model> models = repositoryService.createModelQuery().list();
        dt.setData(models);
        dt.setRecordsFiltered((long)models.size());
        dt.setRecordsTotal((long)models.size());
        return dt;
    }

    public String create(String name, String key, String description) throws UnsupportedEncodingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        Model model = repositoryService.newModel();
        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
        description = StringUtils.defaultString(description);
        modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        model.setMetaInfo(modelObjectNode.toString());
        model.setName(name);
        model.setKey(StringUtils.defaultString(key));

        repositoryService.saveModel(model);
        repositoryService.addModelEditorSource(model.getId(), editorNode.toString().getBytes("utf-8"));
        return model.getId();
    }

    public String deploy(String id) throws IOException {
        Model modelData = repositoryService.getModel(id);
        ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData.getId()));
        byte[] bpmnBytes = null;

        BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
        bpmnBytes = new BpmnXMLConverter().convertToXML(model);

        String processName = modelData.getName() + ".bpmn20.xml";
        Deployment deployment = repositoryService.createDeployment().name(modelData.getName()).addString(processName, new String(bpmnBytes)).deploy();
        return deployment.getId();
    }

    public void delete(String id){
        repositoryService.deleteModel(id);
    }

    public Task getTaskByBussinessId(String id){
        return taskService.createTaskQuery().processInstanceBusinessKey(id).singleResult();
    }

    public HistoricTaskInstance getFinishedTaskByBussinessId(String id){
        return historyService.createHistoricTaskInstanceQuery().processInstanceBusinessKey(id).singleResult();
    }

    public String startWorkflow(String processKey, String key, String uid, Map<String, Object> variables){
        String bussinessKey = key;
        ProcessInstance processInstance = null;
        try{
            identityService.setAuthenticatedUserId(uid);
            processInstance = runtimeService.startProcessInstanceByKey(processKey, bussinessKey, variables);
        }
        finally {
            identityService.setAuthenticatedUserId(null);
        }
        return processInstance.getId();
    }

    public void getTodoTasks(String uid, int pageIndex, int pageSize){
        List<Task> tasks = taskService.createTaskQuery().taskCandidateOrAssigned(uid).listPage(pageIndex*pageSize, pageSize);
    }

}
