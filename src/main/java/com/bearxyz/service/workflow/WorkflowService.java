package com.bearxyz.service.workflow;

import com.bearxyz.common.DataTable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Model;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.List;

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

}
