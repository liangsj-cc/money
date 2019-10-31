layui.use(['layer', 'form', 'table', 'admin', 'ax', 'element'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var element = layui.element;

    const setFrom = () => {
        let answer = $("#answer").val()
        if (answer) {
            let rpa = answer.replace(/\'/g, `"`)
            let data = JSON.parse(rpa)
            form.val('exam-day', data)
        }

    }
    setFrom();

    const formval = () => {
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
        return rs;
    }
    const setpro = (rs) => {
        let a = $("#essize").val();
        $("#pro").attr("lay-percent", `${Object.keys(rs).length} / ${a}`)
        element.progress('exam-progress', `${Object.keys(rs).length} / ${a}`).init()
    }

    form.on('checkbox()', function (data) {
        let rs = formval()
        setpro(rs)
    });
    form.on('radio()', function (data) {
        let rs = formval()
        setpro(rs)
    });


    form.on('submit(btnSubmit)', function (data) {
        let rs = formval()
        let a = $("#essize").val();
        let examHistoryId = $("#examHistoryId").val();
        if (parseInt(a) === Object.keys(rs).length) {
            var ajax = new $ax(Feng.ctxPath + `/exam/comp/${examHistoryId}`, function (data) {
                Feng.success("提交成功");
                window.location.reload();
            }, function (data) {
                Feng.fail("提交失败");
            });
            ajax.set({
                answer: JSON.stringify(data.field),
                rs: JSON.stringify(rs)
            });
            ajax.start();
        }
        return false;
    });

})