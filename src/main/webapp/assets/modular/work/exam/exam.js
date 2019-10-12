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
            {field: 'selector', sort: true, title: '题目选择'},
            {field: 'deptName', sort: true, title: '部门'},
            {field: 'createTime', sort: true, title: '创建时间'},
            {field:'num',sort:true,title:'题目个数'},
            {field:'typeName',sort:true,title:'考试类型'},
            {align: 'center', toolbar: '#tableBar', title: '操作', minWidth: 200}
        ]];
    };


    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Exam.tableId,
        url: Feng.ctxPath + '/exam/list',
        page: true,
        height: "full-98",
        cellMinWidth: 100,
        cols: Exam.initColumn()
    });

});
