/**
 * Created by bearxyz on 2017/6/21.
 */

var CONSTANT = {

    // datatables常量
    DATA_TABLES: {
        DEFAULT_OPTION: { // DataTables初始化选项
            LANGUAGE: {
                sProcessing: "正在查询...",
                sLengthMenu: "_MENU_",
                sZeroRecords: "没有匹配结果",
                sInfo: "显示第 _START_ 至 _END_ 项结果，共 _TOTAL_ 项",
                sInfoEmpty: "显示第 0 至 0 项结果，共 0 项",
                sInfoFiltered: "(由 _MAX_ 项结果过滤)",
                sInfoPostFix: "",
                sSearch: "搜索:",
                sUrl: "",
                sEmptyTable: "暂无数据",
                sLoadingRecords: "载入中...",
                sInfoThousands: ",",
                oPaginate: {
                    sFirst: "首页",
                    sPrevious: "上页",
                    sNext: "下页",
                    sLast: "末页"
                },
                "oAria": {
                    "sSortAscending": ": 以升序排列此列",
                    "sSortDescending": ": 以降序排列此列"
                }
            },
            order: [],
            processing: false,
            serverSide: true,
            searching: false
        },
        COLUMN: {
            CHECKBOX: {
                data: "id",
                width: "20px",
                className: 'smart-form',
                render: function (data, type, row, meta) {
                    return '<label class="checkbox"><input name="check-id" type="checkbox" value="' + data + '" /><i> </i></label>';
                }
            }
        },
        // 常用render可以抽取出来，如日期时间、头像等
        RENDER: {
            DATETIME: function (data, type, row, meta) {
                if(data!=null&&data!="") {
                    return moment(data).format("YYYY/MM/DD HH:mm");
                }
                else
                    return "";
            }
        }
    }


};

function BindSelect(ctrlName, url, placeholder, selected) {
    var control = $('#' + ctrlName);
    $.getJSON(url, function (data) {
        control.empty();
        control.append("<option value=''>&nbsp;" + placeholder + "</option>");
        $.each(data, function (i, item) {
            if(selected&&selected==item.id)
                control.append("<option value='" + item.id + "' selected>&nbsp;" + item.text + "</option>");
            else
                control.append("<option value='" + item.id + "'>&nbsp;" + item.text + "</option>");
        });
    });
}

function BindDictItem(ctrlName, mask, placeholder, selected) {
    var url = '/common/getDict?mask=' + encodeURI(mask);
    BindSelect(ctrlName, url, placeholder, selected);
}

function getRowObj(obj)
{
    var i = 0;
    while(obj.tagName.toLowerCase() != "tr"){
        obj = obj.parentNode;
        if(obj.tagName.toLowerCase() == "table")return null;
    }
    return obj;
}
function getRowNo(obj){
    var trObj = getRowObj(obj);
    var trArr = trObj.parentNode.children;
    for(var trNo= 0; trNo < trArr.length; trNo++){
        if(trObj == trObj.parentNode.children[trNo]){
            alert(trNo+1);
        }
    }
}
function delRow(obj){
    var tr = this.getRowObj(obj);
    if(tr != null){
        tr.parentNode.removeChild(tr);
    }else{
        throw new Error("the given object is not contained by the table");
    }
}

function completeTask(taskId, variables) {
    var keys = "", values = "", types = "";
    if (variables) {
        $.each(variables, function () {
            if (keys != "") {
                keys += ",";
                values += ",";
                types += ",";
            }
            keys += this.key;
            values += this.value;
            types += this.type;
        });
    }
    $.post('/common/task/complete/' + taskId, {keys: keys, values: values, types: types}, function () {
        window.location.reload();
    });
}

function completeTaskRedirect(taskId, variables, url) {
    var keys = "", values = "", types = "";
    if (variables) {
        $.each(variables, function () {
            if (keys != "") {
                keys += ",";
                values += ",";
                types += ",";
            }
            keys += this.key;
            values += this.value;
            types += this.type;
        });
    }
    $.post('/common/task/complete/' + taskId, {keys: keys, values: values, types: types}, function () {
        window.location = url;
    });
}