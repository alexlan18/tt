(function($) {
	/**
	 * 删除节点
	 * @param {Object} target DOM对象
	 */
	function removeNode(target) {
		target.each(function() {
			$(this).remove();
			if ($.browser.msie) {
				this.outerHTML = "";
			}
		});
	}
	;
	/**
	 * 设置panel的width,height,left,top
	 * @param {Object} target DOM对象
	 * @param {Object} param  options属性
	 */
	function setSize(target, param) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var header = panel.children("div.panel-header");
		var pbody = panel.children("div.panel-body");
		if (param) {
			if (param.width) {
				opts.width = param.width;
			}
			if (param.height) {
				opts.height = param.height;
			}
			if (param.left != null) {
				opts.left = param.left;
			}
			if (param.top != null) {
				opts.top = param.top;
			}
		}
		if (opts.fit == true) {
			var p = panel.parent();
			p.addClass("panel-noscroll");
			if (p[0].tagName == "BODY") {
				$("html").addClass("panel-fit");
			}
		}
		panel.css({
			left : opts.left,
			top : opts.top
		});
		if (!isNaN(opts.width)) {
			panel._outerWidth(opts.width);
		} else {
			panel.width("auto");
		}
        //判断是否为IE，然后设置不同的值
        if($.browser.msie) {
            pbody._outerWidth(panel.width() - 2);
        } else {
            pbody._outerWidth(panel.width());
        }
		header.add(pbody);
		if (!isNaN(opts.height)) {
			panel._outerHeight(opts.height);
			pbody._outerHeight(panel.height() - header.outerHeight());
		} else {
			pbody.height("auto");
		}
		panel.css("height", "");
		/**
		 *apply 调用 等同于call,但参数不同
		 */
		opts.onResize.apply(target, [ opts.width, opts.height ]);
		panel.find(">div.panel-body>div").triggerHandler("_resize");
	}
	;
	/**
	 * 移动Panel组件
	 * @param {Object} target DOM对象
	 * @param {Object} param  options属性
	 */
	function movePanel(target, param) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (param) {
			if (param.left != null) {
				opts.left = param.left;
			}
			if (param.top != null) {
				opts.top = param.top;
			}
		}
		panel.css({
			left : opts.left,
			top : opts.top
		});
		opts.onMove.apply(target, [ opts.left, opts.top ]);
	}
	;
	function wrapPanel(target) {
		$(target).addClass("panel-body");
		var panel =  $("<div class=\"panel\"></div>").insertBefore(target);
		panel[0].appendChild(target);
		panel.bind("_resize", function() {
			var opts = $.data(target, "panel").options;
			if (opts.fit == true) {
				setSize(target);
			}
			return false;
		});
		return panel;
	}
	;
	
	/**
	 * 增加Panel组件header
	 * @param {Object} target DOM对象
	 */
	function addHeader(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (opts.tools && typeof opts.tools == "string") {
			panel.find(">div.panel-header>div.panel-tool .panel-tool-a").appendTo(opts.tools);
		}
		removeNode(panel.children("div.panel-header"));
		if (opts.title && !opts.noheader) {
			var header = $("<div class=\"panel-header\"><div class=\"panel-title\">" + opts.title + "</div></div>").prependTo(panel);
			if (opts.iconCls) {
				header.find(".panel-title").addClass("panel-with-icon");
				$("<div class=\"panel-icon\"></div>").addClass(opts.iconCls).appendTo(header);
			}
			var tool = $("<div class=\"panel-tool\"></div>").appendTo(header);
			tool.bind("click", function(e) {
				e.stopPropagation();
			});
			if (opts.tools) {
				if (typeof opts.tools == "string") {
					$(opts.tools).children().each(function() {
						$(this).addClass($(this).attr("iconCls")).addClass("panel-tool-a").appendTo(tool);
					});
				} else {
					for ( var i = 0; i < opts.tools.length; i++) {
						var t = $("<a href=\"javascript:void(0)\"></a>").addClass(opts.tools[i].iconCls).appendTo(tool);
						if (opts.tools[i].handler) {
							t.bind("click", eval(opts.tools[i].handler));
						}
					}
				}
			}
			if (opts.collapsible) {
				$("<a class=\"panel-tool-collapse\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click", function() {
					if (opts.collapsed == true) {
						expandPanel(target, true);
					} else {
						collapsePanel(target, true);
					}
					return false;
				});
			}
			if (opts.minimizable) {
				$("<a class=\"panel-tool-min\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click", function() {
					minimizePanel(target);
					return false;
				});
			}
			if (opts.maximizable) {
				$("<a class=\"panel-tool-max\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click", function() {
					if (opts.maximized == true) {
						restorePanel(target);
					} else {
						maximizePanel(target);
					}
					return false;
				});
			}
			if (opts.closable) {
				$("<a class=\"panel-tool-close\" href=\"javascript:void(0)\"></a>").appendTo(tool).bind("click", function() {
					closePanel(target);
					return false;
				});
			}
			panel.children("div.panel-body").removeClass("panel-body-noheader");
		} else {
			panel.children("div.panel-body").addClass("panel-body-noheader");
		}
	}
	;
	/**
	 * 加载Panel组件数据
	 * @param {Object} target DOM对象
	 */
	function loadData(target) {
		var panel = $.data(target, "panel");
		if (panel.options.href && (!panel.isLoaded || !panel.options.cache)) {
			panel.isLoaded = false;
			destroyNode(target);
			var pbody = panel.panel.find(">div.panel-body");
			if (panel.options.loadingMessage) {
				pbody.html($("<div class=\"panel-loading\"></div>").html(panel.options.loadingMessage));
			}
			$.ajax({
				url : panel.options.href,
				cache : false,
				success : function(data) {
					pbody.html(panel.options.extractor.call(target, data));
					if ($.parser) {
						$.parser.parse(pbody);
					}
					panel.options.onLoad.apply(target, arguments);
					panel.isLoaded = true;
				}
			});
		}
	}
	;
	function destroyNode(target) {
		var t = $(target);
		t.find(".combo-f").each(function() {
			$(this).combo("destroy");
		});
		t.find(".m-btn").each(function() {
			$(this).menubutton("destroy");
		});
		t.find(".s-btn").each(function() {
			$(this).splitbutton("destroy");
		});
	}
	;
	/**
	 *	初始化Panel组件
	 * @param {Object} target DOM对象
	 */
	function init(target) {
		$(target).find("div.panel:visible,div.accordion:visible,div.tabs-container:visible,div.layout:visible").each(function() {
			$(this).triggerHandler("_resize", [ true ]);
		});
	}
	;
	/**
	 *	打开Panel组件
	 * @param {Object} target DOM对象
	 * @param {Object} forceOpen 判断标识是否在panel打开前触发事件
	 */
	function openPanel(target, forceOpen) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (forceOpen != true) {
			if (opts.onBeforeOpen.call(target) == false) {
				return;
			}
		}
		panel.show();
		opts.closed = false;
		opts.minimized = false;
		opts.onOpen.call(target);
		if (opts.maximized == true) {
			opts.maximized = false;
			maximizePanel(target);
		}
		if (opts.collapsed == true) {
			opts.collapsed = false;
			collapsePanel(target);
		}
		if (!opts.collapsed) {
			loadData(target);
			init(target);
		}
	}
	;
	/**
	 *	关闭Panel组件
	 * @param {Object} target DOM对象
	 * @param {Object} forceClose 判断标识是否在panel关闭前触发事件
	 */
	function closePanel(target, forceClose) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (forceClose != true) {
			if (opts.onBeforeClose.call(target) == false) {
				return;
			}
		}
		panel.hide();
		opts.closed = true;
		opts.onClose.call(target);
	}
	;
	/**
	 *	销毁Panel组件
	 * @param {Object} target DOM对象
	 * @param {Object} forceClose 判断标识是否在panel销毁前触发事件
	 */
	function destroyPanel(target, forceDestroy) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		if (forceDestroy != true) {
			if (opts.onBeforeDestroy.call(target) == false) {
				return;
			}
		}		
		destroyNode(target);
		removeNode(panel);
		opts.onDestroy.call(target);
		}
	;
	/**
	 *	折叠Panel组件
	 * @param {Object} target DOM对象
	 * @param {Object} animate boolean
	 */
	function collapsePanel(target, animate) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var pbody = panel.children("div.panel-body");
		var header = panel.children("div.panel-header").find("a.panel-tool-collapse");
		if (opts.collapsed == true) {
			return;
		}
		pbody.stop(true, true);
		if (opts.onBeforeCollapse.call(target) == false) {
			return;
		}
		header.addClass("panel-tool-expand");
		if (animate == true) {
			pbody.slideUp("normal", function() {
				opts.collapsed = true;
				opts.onCollapse.call(target);
			});
		} else {
			pbody.hide();
			opts.collapsed = true;
			opts.onCollapse.call(target);
		}
	}
	;
	/**
	 *	展开Panel组件
	 * @param {Object} target DOM对象
	 * @param {Object} animate boolean
	 */
	function expandPanel(target, animate) {
		var opts = $.data(target, "panel").options;
		var panle = $.data(target, "panel").panel;
		var pbody = panle.children("div.panel-body");
		var tool = panle.children("div.panel-header").find("a.panel-tool-collapse");
		if (opts.collapsed == false) {
			return;
		}
		pbody.stop(true, true);
		if (opts.onBeforeExpand.call(target) == false) {
			return;
		}
		tool.removeClass("panel-tool-expand");
		if (animate == true) {
			pbody.slideDown("normal", function() {
				opts.collapsed = false;
				opts.onExpand.call(target);
				loadData(target);
				init(target);
			});
		} else {
			pbody.show();
			opts.collapsed = false;
			opts.onExpand.call(target);
			loadData(target);
			init(target);
		}
	}
	;
	/**
	 *	最大化Panel组件
	 * @param {Object} target DOM对象
	 * 
	 */
	function maximizePanel(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var tool = panel.children("div.panel-header").find("a.panel-tool-max");
		if (opts.maximized == true) {
			return;
		}
		tool.addClass("panel-tool-restore");
		if (!$.data(target, "panel").original) {
			$.data(target, "panel").original = {
				width : opts.width,
				height : opts.height,
				left : opts.left,
				top : opts.top,
				fit : opts.fit
			};
		}
		opts.left = 0;
		opts.top = 0;
        opts.width = $(panel.parent()).width();
        opts.height = $(panel.parent())[0].scrollHeight;
		opts.fit = true;
		setSize(target);
		opts.minimized = false;
		opts.maximized = true;
		opts.onMaximize.call(target);
	}
	;
	/**
	 *	最小化Panel组件
	 * @param {Object} target DOM对象
	 * 
	 */
	function minimizePanel(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		panel.hide();
		opts.minimized = true;
		opts.maximized = false;
		opts.onMinimize.call(target);
	}
	;
	/**
	 *	恢复原始Panel组件
	 * @param {Object} target DOM对象
	 * 
	 */
	function restorePanel(target) {
		var opts = $.data(target, "panel").options;
		var panle = $.data(target, "panel").panel;
		var tool = panle.children("div.panel-header").find("a.panel-tool-max");
		if (opts.maximized == false) {
			return;
		}
		panle.show();
		tool.removeClass("panel-tool-restore");
		var original = $.data(target, "panel").original;
		opts.width = original.width;
		opts.height = original.height;
		opts.left = original.left;
		opts.top = original.top;
		opts.fit = original.fit;
		setSize(target);
		opts.minimized = false;
		opts.maximized = false;
		$.data(target, "panel").original = null;
		opts.onRestore.call(target);
	}
	;
	/**
	 *	设置Panel组件border
	 * @param {Object} target DOM对象
	 * 
	 */
	function setBorder(target) {
		var opts = $.data(target, "panel").options;
		var panel = $.data(target, "panel").panel;
		var header = $(target).panel("header");
		var pbody = $(target).panel("body");
		panel.css(opts.style);
		panel.addClass(opts.cls);
		if (opts.border == true) {
			header.removeClass("panel-header-noborder");
			pbody.removeClass("panel-body-noborder");
		} else {
			header.addClass("panel-header-noborder");
			pbody.addClass("panel-body-noborder");
		}
		header.addClass(opts.headerCls);
		pbody.addClass(opts.bodyCls);
		if (opts.id) {
			$(target).attr("id", opts.id);
		} else {
			$(target).attr("id", "");
		}
	}
	;
	/**
	 *	设置Panel组件title
	 * @param {Object} target DOM对象
	 * 
	 */
	function setTitle(target, title) {
		$.data(target, "panel").options.title = title;
		$(target).panel("header").find("div.panel-title").html(title);
	}
	;
	var TO = false;
	var resized = true;
	$(window).unbind(".panel").bind("resize.panel", function() {
		if (!resized) {
			return;
		}
		if (TO !== false) {
			clearTimeout(TO);
		}
		TO = setTimeout(function() {
			resized = false;
			var layout = $("body.layout");
			if (layout.length) {
				layout.layout("resize");
			} else {
				$("body").children("div.panel,div.accordion,div.tabs-container,div.layout").triggerHandler("_resize");
			}
			resized = true;
			TO = false;
		}, 200);
	});
	$.fn.panel = function(options, param) {
		if (typeof options == "string") {
			return $.fn.panel.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "panel");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, $.fn.panel.defaults, $.fn.panel.parseOptions(this), options);
				$(this).attr("title", "");
				state = $.data(this, "panel", {
					options : opts,
					panel : wrapPanel(this),
					isLoaded : false
				});
			}
			if (opts.content) {
				$(this).html(opts.content);
				if ($.parser) {
					$.parser.parse(this);
				}
			}
			addHeader(this);
			setBorder(this);
			if (opts.doSize == true) {
				state.panel.css("display", "block");
				setSize(this);
			}
			if (opts.closed == true || opts.minimized == true) {
				state.panel.hide();
			} else {
				openPanel(this);
			}
		});
	};
	/**
	 * 注册方法
	 */
	$.fn.panel.methods = {
		options : function(jq) {
			return $.data(jq[0], "panel").options;
		},
		panel : function(jq) {
			return $.data(jq[0], "panel").panel;
		},
		header : function(jq) {
			return $.data(jq[0], "panel").panel.find(">div.panel-header");
		},
		body : function(jq) {
			return $.data(jq[0], "panel").panel.find(">div.panel-body");
		},
		setTitle : function(jq, title) {
			return jq.each(function() {
				setTitle(this, title);
			});
		},
		open : function(jq, param) {
			return jq.each(function() {
				openPanel(this, param);
			});
		},
		close : function(jq, param) {
			return jq.each(function() {
				closePanel(this, param);
			});
		},
		destroy : function(jq, param) {
			return jq.each(function() {
				destroyPanel(this, param);
			});
		},
		refresh : function(jq, url) {
			return jq.each(function() {
				$.data(this, "panel").isLoaded = false;
				if (url) {
					$.data(this, "panel").options.href = url;
				}
				loadData(this);
			});
		},
		resize : function(jq, param) {
			return jq.each(function() {
				setSize(this, param);
			});
		},
		move : function(jq, param) {
			return jq.each(function() {
				movePanel(this, param);
			});
		},
		maximize : function(jq) {
			return jq.each(function() {
				maximizePanel(this);
			});
		},
		minimize : function(jq) {
			return jq.each(function() {
				minimizePanel(this);
			});
		},
		restore : function(jq) {
			return jq.each(function() {
				restorePanel(this);
			});
		},
		collapse : function(jq, param) {
			return jq.each(function() {
				collapsePanel(this, param);
			});
		},
		expand : function(jq, param) {
			return jq.each(function() {
				expandPanel(this, param);
			});
		}
	};
	$.fn.panel.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [ "id", "width", "height", "left", "top", "title", "iconCls", "cls", "headerCls", "bodyCls", "tools", "href", {
			cache : "boolean",
			fit : "boolean",
			border : "boolean",
			noheader : "boolean"
		}, {
			collapsible : "boolean",
			minimizable : "boolean",
			maximizable : "boolean"
		}, {
			closable : "boolean",
			collapsed : "boolean",
			minimized : "boolean",
			maximized : "boolean",
			closed : "boolean"
		} ]), {
			loadingMessage : (t.attr("loadingMessage") != undefined ? t.attr("loadingMessage") : undefined)
		});
	};
	$.fn.panel.defaults = {
		id : null,
		title : null,
		iconCls : null,
		width : "auto",
		height : "auto",
		left : null,
		top : null,
		cls : null,
		headerCls : null,
		bodyCls : null,
		style : {},
		href : null,
		cache : true,
		fit : false,
		border : true,
		doSize : true,
		noheader : false,
		content : null,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		closable : false,
		collapsed : false,
		minimized : false,
		maximized : false,
		closed : false,
		tools : null,
		href : null,
		loadingMessage : "Loading...",
		extractor : function(data) {
			var bodyRegEx = /<body[^>]*>((.|[\n\r])*)<\/body>/im;
			var found = bodyRegEx.exec(data);
			if (found) {
				return found[1];
			} else {
				return data;
			}
		},
		onLoad : function() {
		},
		onBeforeOpen : function() {
		},
		onOpen : function() {
		},
		onBeforeClose : function() {
		},
		onClose : function() {
		},
		onBeforeDestroy : function() {
		},
		onDestroy : function() {
		},
		onResize : function(width, height) {
		},
		onMove : function(left, top) {
		},
		onMaximize : function() {
		},
		onRestore : function() {
		},
		onMinimize : function() {
		},
		onBeforeCollapse : function() {
		},
		onBeforeExpand : function() {
		},
		onCollapse : function() {
		},
		onExpand : function() {
		}
	};
})(jQuery);