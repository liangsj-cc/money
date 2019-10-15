layui.use(['layer', 'form', 'table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;

    /**
     * 系统管理--消息管理
     */
    var Exam = {
        tableId: "examTable"    //表格id
    };

    /**
     * 初始化表格的列
     */
    Exam.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: true, sort: true, title: 'id'},
            {field: 'name', sort: true, title: '标题'},
            {field: 'selector', templet: "#selector", title: '题目选择'},
            {field: 'deptName', sort: true, title: '部门'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {field: 'num', sort: true, title: '题目个数'},
            {field: 'type', sort: true, templet: '#type', title: '考试类型'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


    Exam.search = function () {
        let queryData = {
            name: $("#examName").val(),
            type: $("#examType option:selected").val()
        };
        console.log(queryData)
        table.reload(Exam.tableId, {where: queryData});
    }

    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Exam.tableId,
        url: Feng.ctxPath + '/exam/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: Exam.initColumn()
    });
    Exam.onAddExam = () => {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '添加考试',
            content: Feng.ctxPath + '/exam/add',
            area: ['360px', '600px'],
            end: function () {
                admin.getTempData('formOk') && table.reload(Exam.tableId);
            }
        });

    }

    $("#btnSearch").click(() => {
        Exam.search()
    })

    $("#btnAdd").click(() => {
        Exam.onAddExam()
    })

    Exam.onDeleteExam = (data) => {
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/exam/delete", function () {
                table.reload(Exam.tableId);
                Feng.success("删除成功!");
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("id", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除习题" + data.name + "?", operation);

    }
    Exam.onEditExam = (data) => {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '编辑用户',
            content: Feng.ctxPath + '/exam/exam_edit?id=' + data.id,
            end: function () {
                admin.getTempData('formOk') && table.reload(Exam.tableId);
            }
        });
    }

    table.on('tool(' + Exam.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;

        if (layEvent === 'edit') {
            Exam.onEditExam(data);
        } else if (layEvent === 'delete') {
            Exam.onDeleteExam(data);
        }
    });
});
