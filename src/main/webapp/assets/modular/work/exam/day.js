layui.use(['layer', 'form', 'table', 'admin', 'ax', 'element'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var element = layui.element;

    const formval = () => {
        let res = [];
        let ds = form.val('exam-day')
        let rs = {}
        for (let key in ds) {
            if (null !== key.match(/\[\d{0,2}\]/)) {
                let nk = key.substr(0, 19);
                if (rs[nk]) {
                    rs[nk].push(ds[key])
                } else {
                    rs[nk] = [ds[key]]
                }
            } else {
                rs[key] = ds[key]
            }

        }
        let a = $("#essize").val();
        $("#pro").attr("lay-percent", `${Object.keys(rs).length} / ${a}`)
        element.progress('exam-progress', `${Object.keys(rs).length} / ${a}`).init()
        return rs;
    }

    form.on('checkbox()', function (data) {
        let rs = formval()

    });
    form.on('radio()', function (data) {
        let rs = formval()
    });


    form.on('submit(btnSubmit)', function (data) {
        console.log(data.field)

        return false;
    });

})