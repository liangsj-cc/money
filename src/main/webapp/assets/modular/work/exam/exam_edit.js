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

    var ajax = new $ax(Feng.ctxPath + "/exam/getExamInfo?id=" + Feng.getUrlParam("id"));
    var result = ajax.start();
    let res = {...result.data}
    let sejson = JSON.parse(res.selector)
    console.log(sejson)
    let toForm = Object.keys(sejson).map((i, index) => {
        let a = {}
        a[`key-${index}`] = i
        a[`val-${index}`] = sejson[i]
        return a
    }).flat(0)
        .reduce((a, b) => {
            return {...a, ...b}
        })
    form.val('examForm', {...res, ...toForm});


    $('#deptName').click(function () {
        var formName = encodeURIComponent("parent.UserInfoDlg.data.deptName");
        var formId = encodeURIComponent("parent.UserInfoDlg.data.deptId");
        var treeUrl = encodeURIComponent("/dept/tree");

        layer.open({
            type: 2,
            title: '部门选择',
            area: ['300px', '400px'],
            content: Feng.ctxPath + '/system/commonTree?formName=' + formName + "&formId=" + formId + "&treeUrl=" + treeUrl,
            end: function () {
                console.log(UserInfoDlg.data);
                $("#deptId").val(UserInfoDlg.data.deptId);
                $("#deptName").val(UserInfoDlg.data.deptName);
            }
        });
    });

    form.on('submit(btnSubmit)', function (data) {
        console.log(data.field)
        let ajax = new $ax(Feng.ctxPath + "/exam/edit/json", function (data) {
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

})