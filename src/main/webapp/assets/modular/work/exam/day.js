layui.use(['layer', 'form', 'table', 'admin', 'ax', 'element'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var element = layui.element;

    const formval = () => {
        let ds = form.val('exam-day')
        console.log(ds)
    }

    form.on('checkbox()', function (data) {
        formval()

    });
    form.on('radio()', function (data) {
        let ds = form.val('exam-day')
        console.log(ds)
    });


    form.on('submit(btnSubmit)', function (data) {
        console.log(data.field)
        return false;
    });

})