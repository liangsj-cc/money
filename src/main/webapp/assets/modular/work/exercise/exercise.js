layui.use(['layer', 'form', 'table', 'admin', 'ax'], function () {
    var $ = layui.$;
    var layer = layui.layer;
    var form = layui.form;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;

    /**
     * 系统管理--消息管理
     */
    var Exercise = {
        tableId: "exerciseTable",  //表格id
    };


    /** 初始化状态 表格 **/
    Exercise.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: false, sort: true, title: 'id'},
            {field: 'name', sort: true, title: '题目'},
            {field: 'options', templet: '#options', title: '选项'},
            {field: 'rights', hide: true, title: '正确答案'},
            {field: 'label', templet: "#labels", title: '标签'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    Exercise.search = function () {
        let queryData = {};
        for (let i = 0; i < 3; i++) {
            queryData[$(`#label-${i}`).val()] = $(`#key-${i}`).val()
        }
        table.reload(Exercise.tableId, {where: {lables: JSON.stringify(queryData)}});
    }
    // // 渲染表格
    var tableResult = table.render({
        elem: '#' + Exercise.tableId,
        url: Feng.ctxPath + '/exercise/list',
        page: true,
        height: "full-136",
        cellMinWidth: 100,
        cols: Exercise.initColumn()
    });

    // 工具条点击事件
    table.on('tool(' + Exercise.tableId + ')', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        if (layEvent === 'delete') {
            Exercise.onDeleteExercise(data);
        } else if (layEvent === 'showInfo') {
            console.log(data.id)
            top.layui.admin.open({
                type: 2,
                title: '习题详情',
                area: ['600px', '480px'],
                content:`${Feng.ctxPath}/exercise/info/${data.id}`,
            });
        }
    });

    Exercise.onDeleteExercise = function (data) {
        console.log(123)
        var operation = function () {
            var ajax = new $ax(Feng.ctxPath + "/exercise/delete", function (data) {
                Feng.success("删除成功!");
                table.reload(Exercise.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.responseJSON.message + "!");
            });
            ajax.set("execriseId", data.id);
            ajax.start();
        };
        Feng.confirm("是否删除题目 ?", operation);
    };


    Exercise.openAddExercise = function () {
        admin.putTempData('formOk', false);
        top.layui.admin.open({
            type: 2,
            title: '导入习题',
            content: Feng.ctxPath + '/exercise/exercise_import',
            area: ['600px', '480px'],
            end: function () {
                admin.getTempData('formOk') && table.reload(Exercise.tableId);
            }
        });
    };


    $("#btnSearch").click(function () {
        Exercise.search()
    })

    $('#btnImport').click(function () {
        Exercise.openAddExercise();
    });

});
