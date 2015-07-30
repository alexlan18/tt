<%@page contentType="text/html;charset=UTF-8" %>
<%@page pageEncoding="UTF-8" %>
<%@ page isELIgnored="false" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="mainLayout" class="easyui-layout" fit="true">
    <div region="west" split="true" title="主菜单" style="width: 250px;border:0;border:1px solid #d4d4d4\9;">
        <div id="menu" class="easyui-accordion" fit="true">
        </div>
    </div>
    <div region="center" split="true" fit="true" iconCls="icon-grid" style="width: auto;">
        <div id="bottomPanel">
        </div>
        <div id="tabSubPanel" class="easyui-menu" style="width: 150px;">
            <div id="tabSubPanel-refresh">刷新</div>
            <div id="tabSubPanel-closeall">全部关闭</div>
            <div id="tabSubPanel-closeother">关闭其它</div>
        </div>
    </div>
</div>
<script type="text/javascript">

    $(function () {
        //加载菜单信息
        $('#menu').accordion("loadMenus", {
            'url': "<c:url value='/system/sMenus/list'/>",
            'success': function () {
                //点击菜单
                $('#menu ul li').click(function () {

                    $('#menu ul li').removeClass("tree-node-selected");
                    $(this).addClass("tree-node-selected");

                    //获取菜单名称和URL
                    var menuName = $(this).find(".tree-title").html();
                    var url = $(this).find(".tree-url").html();
                    var menuId = $(this).find(".tree-menuId").html();

                    //判断菜单选项卡是否打开，如果没有则添加
                    if ($('#bottomPanel').tabs('existsById', menuId)) {
                        $('#bottomPanel').tabs('selectById', menuId);
                    } else {
                        $('#bottomPanel').tabs('add', {
                            tid: menuId,
                            title: menuName,
                            content: createFrame(url),
                            selected: true,
                            closable: true
                        });
                    }
                });
            }
        });

        //初始化Tab信息
        var width = document.body.clientWidth;
        var height = document.body.clientHeight;
        $('#bottomPanel').width(width - 6);
        $('#bottomPanel').height(height - 106);

        //初始化标签
        $('#bottomPanel').tabs({
            fit: true,
            border: false
        });

        //添加首页
        $('#bottomPanel').tabs('add', {
            title: "系统首页",
            content: createFrame(_ContextPath + "/mainPage.jsp"),
            selected: true,
            closable: false
        });

        //右键菜单
        $('#tabSubPanel-refresh').click(function () {
            var selectedTab = $('#bottomPanel').tabs('getSelected');
            var url = $(selectedTab.panel('options').content).attr('src');
            $('#bottomPanel').tabs('update', {
                tab: selectedTab,
                options: {content: createFrame(url)}
            });
        });
        $('#tabSubPanel-closeall').click(function () {
            $(".tabs li").each(function (i, n) {
                var title = $(n).text();
                var tab = $('#bottomPanel').tabs('getTab', title);
                //不关首页选项卡
                if (tab.panel("options").tid) {
                    $('#bottomPanel').tabs('close', title);
                }
            });
        });
        $('#tabSubPanel-closeother').click(function () {
            var selectedTab = $('#bottomPanel').tabs('getSelected');
            $(".tabs li").each(function (i, n) {
                var title = $(n).text();
                if (title != selectedTab.panel('options').title) {
                    $('#bottomPanel').tabs('close', title);
                }
            });
        });

        /*为选项卡绑定右键*/
        $(".tabs li").live('contextmenu', function (e) {
            /* 选中当前触发事件的选项卡 */
            var subtitle = $(this).text();
            $('#bottomPanel').tabs('select', subtitle);

            //显示快捷菜单
            $('#tabSubPanel').menu('show', {
                left: e.pageX,
                top: e.pageY
            });
            return false;
        });
    });

    //创建一个Iframe，放置新页面
    function createFrame(url) {
        var s = '<iframe class="tabIframe" scrolling="no" frameborder="0" marginwidth="0px" marginheight="0px"  src="' + url + '" style="width:100%; height:600px;margin:0;padding:0;positon:relative;display:block;top:0"></iframe>';
        return s;
    }

    $(".tabIframe").load(function() {
        setInterval(function() {
            $(".tabIframe").each(function() {
                var mainHeight = $(this).contents().find("body").height();
                if(mainHeight < 600) {
                    mainHeight = 600;
                }
                $(this).height(mainHeight);
            });
        }, 200);
    });
</script>
