/**
 * 用户详情对话框
 */
var UserInfoDlg = {
    data: {
        deptId: "",
        deptName: ""
    }
};

layui.use(['layer', 'form', 'admin', 'laydate', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();


    $('#deptName').click(function () {
        var formName = encodeURIComponent("parent.UserInfoDlg.data.deptName");
        var formId = encodeURIComponent("parent.UserInfoDlg.data.deptId");
        var treeUrl = encodeURIComponent("/dept/tree");

        layer.open({
            type: 2,
            title: '部门选择',
            area: ['300px', '300px'],
            content: Feng.ctxPath + '/system/commonTree?formName=' + formName + "&formId=" + formId + "&treeUrl=" + treeUrl,
            end: function () {
                $("#deptId").val(UserInfoDlg.data.deptId);
                $("#deptName").val(UserInfoDlg.data.deptName);
            }
        });
    });


    form.on('submit(btnSubmit)', function (data) {
        console.log(data.field)
        let ajax = new $ax(Feng.ctxPath + "/exam/add/json", function (data) {
            Feng.success("添加成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        let selector = {}

        if(data.field['key-0'].length > 0){
            selector[data.field['key-0']] = data.field['val-0']
        }

        if (data.field['key-1'].length > 0) {
            selector[data.field['key-1']] = data.field['val-1']
        }
        if (data.field['key-2'].length > 0) {
            selector[data.field['key-2']] = data.field['val-2']
        }

        data.field.selector = JSON.stringify(selector)
        console.log(data.field)
        ajax.set(data.field);
        ajax.start();
    });
});