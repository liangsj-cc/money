
layui.use(['layer', 'form', 'admin', 'laydate', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;

    // 让当前iframe弹层高度适应
    admin.iframeAuto();

    $("#btnDowload").click(()=>{
        window.location.href=Feng.ctxPath + "/exercise/export";
    })



    layui.use(['element','upload'], function() {
        element = layui.element;
        upload = layui.upload;

        //指定允许上传的文件类型
        upload.render({
            elem: '#uploadExcel'
            ,url: Feng.ctxPath+'/exercise/importExcle'
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