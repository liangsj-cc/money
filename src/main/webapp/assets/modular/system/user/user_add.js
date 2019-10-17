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

    // 点击部门时
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

    // 添加表单验证方法
    form.verify({
        psw: [/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格'],
        repsw: function (value) {
            if (value !== $('#userForm input[name=password]').val()) {
                return '两次密码输入不一致';
            }
        }
    });

    // 渲染时间选择框
    laydate.render({
        elem: '#birthday'
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/mgr/add", function (data) {
            Feng.success("添加成功！");

            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);

            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("添加失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
    });

    //下载人员导入模板
    $('#btnDowload').click(function () {
        window.location.href=Feng.ctxPath + "/mgr/export";
    });


    //倒数数据库
    layui.use(['element','upload'], function() {
        element = layui.element;
        upload = layui.upload;

        //指定允许上传的文件类型
        upload.render({
            elem: '#uploadExcel'
            ,url: Feng.ctxPath+'/mgr/importExcle'
            ,accept: 'file' //普通文件
            ,exts: 'xls|xlsx' //只允许上传Excle文件
            ,multiple: true
            ,done: function(res){
                console.log(res);
                if(res.code == "2001"){
                    Feng.error("上传失败！文件不能为空" );
                }else{
                    Feng.success("上传成功！");
                    admin.putTempData('formOk', true);
                    //关掉对话框
                    admin.closeThisDialog();
                }
            }
        });
    });

});