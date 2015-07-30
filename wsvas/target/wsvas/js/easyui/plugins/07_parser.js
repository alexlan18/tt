/**
 * parser模块主要是解析页面中easyui的控件
 */
(function($) {

	// 定义一个类，
	$.parser = {
		// 是否自动解析easyui组件
		auto : true,

		// 当解析器完成本次解析动作后 调用
		onComplete : function(context) {
		},

		// 可以被解析的控件
		plugins : [ "dropdown", "command", "draggable", "droppable", "resizable", "pagination", "linkbutton", "menu", "menubutton", "splitbutton", "progressbar", "tree", "combobox", "combotree", "combogrid", "numberbox", "validatebox", "searchbox", "numberspinner",
				"timespinner", "calendar", "datebox", "datetimebox", "slider", "layout", "panel", "datagrid", "propertygrid", "treegrid", "tabs", "accordion", "window", "dialog" ],

		// easyui全局解析函数
		parse : function(context) {
			var startTime = log.getTime();
			for ( var i = 0; i < $.parser.plugins.length; i++) {
				// easyui组件名称
				var pluginName = $.parser.plugins[i];

				// 查找class为easyui-控件名的jq对象，例如，easyui-datagrid
				// selector:用来查找的字符串,context:作为待查找的 DOM 元素集、文档或 jQuery 对象。
				var jqObject = $(".easyui-" + pluginName, context);

				// 如果有这个对象，那么判断它有没有初始化函数
				if (jqObject.length && jqObject[pluginName]) {
					// 如果有直接调用这个plugin的自执行方法
					jqObject[pluginName]();
					// alert(pluginName);
				}
				var endTime = log.getTime();
				if(endTime - startTime > 0) log.info("parse plugin [" + pluginName + "] ",endTime - startTime);
				startTime = endTime;
			}
			$.parser.onComplete.call($.parser, context);
		},
		parseOptions : function(target, _7) {
			var t = $(target);
			var _8 = {};
			var s = $.trim(t.attr("data-options"));
			if (s) {
				var _9 = s.substring(0, 1);
				var _a = s.substring(s.length - 1, 1);
				if (_9 != "{") {
					s = "{" + s;
				}
				if (_a != "}") {
					s = s + "}";
				}
				_8 = (new Function("return " + s))();
			}
			if (_7) {
				var _b = {};
				for ( var i = 0; i < _7.length; i++) {
					var pp = _7[i];
					if (typeof pp == "string") {
						if (pp == "width" || pp == "height" || pp == "left" || pp == "top") {
							_b[pp] = parseInt(target.style[pp]) || undefined;
						} else {
							_b[pp] = t.attr(pp);
						}
					} else {
						for ( var _c in pp) {
							var _d = pp[_c];
							if (_d == "boolean") {
								_b[_c] = t.attr(_c) ? (t.attr(_c) == "true") : undefined;
							} else {
								if (_d == "number") {
									_b[_c] = t.attr(_c) == "0" ? 0 : parseFloat(t.attr(_c)) || undefined;
								}
							}
						}
					}
				}
				$.extend(_8, _b);
			}
			return _8;
		}
	};

	// 每个页面加载完都会执行此方法。是$(document).ready(function(){ })的简写方式
	$(function() {
		if ($.parser.auto) {
			$.parser.parse(); // 解析所有页面
		}
	});
	$.fn._outerWidth = function(_e) {
		return this.each(function() {
			if (!$.boxModel && $.browser.msie) {
				$(this).width(_e);
			} else {
				$(this).width(_e - ($(this).outerWidth() - $(this).width()));
			}
		});
	};
	$.fn._outerHeight = function(_f) {
		return this.each(function() {
			if (!$.boxModel && $.browser.msie) {
				$(this).height(_f);
			} else {
				$(this).height(_f - ($(this).outerHeight() - $(this).height()));
			}
		});
	};
})(jQuery);
