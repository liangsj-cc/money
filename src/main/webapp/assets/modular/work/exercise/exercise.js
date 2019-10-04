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
        tableId: "exerciseTable"    //表格id
    };


    /** 初始化状态 表格 **/
    Exercise.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'id', hide: false, sort: true, title: 'id'},
            {field: 'name', sort: true, title: '题目'},
            {field: 'options', sort: true, title: '选项'},
            {field: 'rights', sort: true, title: '正确答案'},
            {field: 'label', sort: true, title: '标签'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };

    Exercise.search = function () {
        let queryData = {};
        for (let i = 0; i < 3; i++) {
            queryData[$(`#label-${i}`).val()] = $(`#key-${i}`).val()
        }
        table.reload(Exercise.tableId, {where: {lables:JSON.stringify(queryData)}});
    }
    // // 渲染表格
    var tableResult = table.render({
        elem: '#' + Exercise.tableId,
        url: Feng.ctxPath + '/exercise/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: Exercise.initColumn()
    });

    $("#labelInputAdd").click(function () {

        console.log(123)
    })

    $("#btnSearch").click(function () {
        console.log(123)
        Exercise.search()
    })

});
