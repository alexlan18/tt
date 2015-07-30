(function($) {
	/**
	 * 调整大小
	 * 
	 * @param {Object}
	 *            jq
	 */
	function resize(jq) {
		var options = $.data(jq, "accordion").options;
		var panels = $.data(jq, "accordion").panels;
		var cc = $(jq);
		// 自适应
		if (options.fit == true) {
			var p = cc.parent();
			p.addClass("panel-noscroll");
			if (p[0].tagName == "BODY") {
				$("html").addClass("panel-fit");
			}
			options.width = p.width();
			options.height = p.height();
		}
		// 宽度设置
		if (options.width > 0) {
			cc._outerWidth(options.width);
		}
		// 高度设置
		var height = "auto";
		if (options.height > 0) {
			cc._outerHeight(options.height);
			var outerHeight = panels.length ? panels[0].panel("header").css("height", "").outerHeight() : "auto";
			var height = cc.height() - (panels.length - 1) * outerHeight;
		}
		// 循环各panel
		for ( var i = 0; i < panels.length; i++) {
			var panel = panels[i];
			var header = panel.panel("header");
			//header._outerHeight(outerHeight);
			panel.panel("resize", {
				width : cc.width(),
				height : height
			});
		}
	}
	;
	/**
	 * 获取被选中的Panel
	 */
	function getSelectedPanel(jq) {
		var panels = $.data(jq, "accordion").panels;
		for ( var i = 0; i < panels.length; i++) {
			var panel = panels[i];
			if (panel.panel("options").collapsed == false) {
				return panel;
			}
		}
		return null;
	}
	;
	
	/**
	 * add since 1.3
	 * 获取被选中的PanelIndex
	 */	
	function getPanelIndex(jq, panel) {
		var panels = $.data(jq, "accordion").panels;
		for ( var i = 0; i < panels.length; i++) {
			if (panels[i][0] == $(panel)[0]) {
				return i;
			}
		}
		return -1;
	}
	/** @param {Object}
	 *            which  (title 或者index)
	 * @param {Object}
	 *            isDelete 获取完后是否删除掉该panel
	 * @return {TypeName} panel
	 */
	function getPanelByWhich(jq, which, isDelete) {
		var panels = $.data(jq, "accordion").panels;
		if (typeof which == "number") {
			if (which< 0 || which >= panels.length) {
				return null;
			} else {
				var panel = panels[which];
				if (isDelete) {
					panels.splice(which, 1);
				}
				return panel;
			}
		}
		for ( var i = 0; i < panels.length; i++) {
			var panel = panels[i];
			if (panel.panel("options").title == which) {
				if (isDelete) {
					// 删除第i个panel
					panels.splice(i, 1);// splice()方法用于插入、删除或替换数组的元素，详细参考http://www.w3school.com.cn/js/jsref_splice.asp
				}
				return panel;
			}
		}
		return null;
	}
	;
	function removeBorderClass(jq) {
		var options = $.data(jq, "accordion").options;
		var cc = $(jq);
        	// 设置边框
		if (options.border) {
			cc.removeClass("accordion-noborder");
		} else {			
		        cc.addClass("accordion-noborder");
		}
  	 };
	/**
	 * 渲染折叠面板
	 * 
	 * @param {Object}
	 *            jq
	 * @memberOf {TypeName}
	 * @return {TypeName}
	 */
	function renderAccordion(jq) {
		var cc = $(jq);
		cc.addClass("accordion");
		var panels = [];
		// 循环所有面板
		cc.children("div").each(function() {
			var options = $.extend({}, $.parser.parseOptions(this), {
				selected : ($(this).attr("selected") ? true : undefined)
			});
			var pp = $(this);
			panels.push(pp);
			createNewPanel(jq, pp, options);// 创建面板
		});

		// 自适应
            cc.bind("_resize", function(e, fit) {
			var options = $.data(jq, "accordion").options;
			if (options.fit == true || fit) {
				resize(jq);
			}
			return false;
		});
		return {
			accordion : cc,
			panels : panels
		};
	}
	;
	/**
	 * 创建新增的Panel
	 * 
	 * @param {Object}
	 *            jq
	 * @param {Object}
	 *            pp 新增Panel的div元素对象
	 * @param {Object}
	 *            options
	 * @memberOf {TypeName}
	 * @return {TypeName}
	 */
	function createNewPanel(jq, pp, options) {
		// 初始化panel
		pp.panel($.extend({},options,
		{
					collapsible : false,
								minimizable : false,
								maximizable : false,
								closable : false,
								doSize : false,
								collapsed : true,
								headerCls : "accordion-header",
								bodyCls : "accordion-body",
									onBeforeExpand : function() {
										var selectedPanel = getSelectedPanel(jq);// 获取展开前被选中的panel
										if (selectedPanel) {
											var header = $(selectedPanel).panel("header");
											header.removeClass("accordion-header-selected");// 把展开前被选中的panel的选中样式设置为未选中
											header.find(".accordion-collapse")
													.triggerHandler("click");// 把展开前被选中的panel合上
										}
										var header = pp.panel("header");
										header.addClass("accordion-header-selected");// 样式设置为已选中
										header.find(".accordion-collapse").removeClass("accordion-expand");// 样式设置为已展开
									},
									onExpand : function() {
										var options = $.data(jq, "accordion").options;
										options.onSelect.call(jq, pp
												.panel("options").title);// 返回选择的panle的title给onSelect事件作为参数，并响应事件
									},
									onBeforeCollapse : function() {
										var header = pp.panel("header");
										header.removeClass("accordion-header-selected");// 去掉选中样式
										header.find(".accordion-collapse").addClass("accordion-expand");// 样式设置为未展开
									}
								}));
		var header = pp.panel("header");
		var t = $(
				"<a class=\"accordion-collapse accordion-expand\" href=\"javascript:void(0)\"></a>")
				.appendTo(header.children("div.panel-tool"));
		t.bind("click", function(e) {
			var animate = $.data(jq, "accordion").options.animate;
			stopAll(jq);
			if (pp.panel("options").collapsed) {
				pp.panel("expand", animate);
			} else {
				pp.panel("collapse", animate);
			}
			return false;
		});
		header.click(function() {
			$(this).find(".accordion-collapse").triggerHandler("click");
			return false;
		});
	}
	;
	function selectByWhich(jq, which) {
		var selectedPanel = getPanelByWhich(jq, which);
		if (!selectedPanel) {
			return;
		}
		var panel = getSelectedPanel(jq);
		if (panel && panel[0] == selectedPanel[0]) {
			return;
		}
		selectedPanel.panel("header").triggerHandler("click");
	}
	;
	function selectByTitle(jq) {
		var options = $.data(jq, "accordion").options;
    		var panels = $.data(jq, "accordion").panels;
		for ( var i = 0; i < panels.length; i++) {
			if (panels[i].panel("options").selected) {
				selectByIndex(i);
				return;
			}
		}
		if (panels.length) {
			selectByIndex(0);
		}
		function selectByIndex(length) {
			var options = $.data(jq, "accordion").options;
			var animate = options.animate;
			options.animate = false;
			selectByWhich(jq, length);
			options.animate = animate;
		}
		;
	}
	;
	/**
	 * 停止折叠面板所有正在运行的动画
	 * 
	 * @param {Object}
	 *            jq
	 */
	function stopAll(jq) {
		var panels = $.data(jq, "accordion").panels;
		for ( var i = 0; i < panels.length; i++) {
			panels[i].stop(true, true);
		}
	}
	;
	/**
	 * 新增Panel
	 * 
	 * @param {Object}
	 *            jq
	 * @param {Object}
	 *            options
	 */
	function add(jq, options) {
		var v_options = $.data(jq, "accordion").options;
		var panels = $.data(jq, "accordion").panels;			
		if (options.selected == undefined) {
			options.selected = true;
		}
		stopAll(jq);
		var pp = $("<div></div>").appendTo(jq);// 新增Panel的div元素对象
		panels.push(pp);
		createNewPanel(jq, pp, options);
		resize(jq);
		v_options.onAdd.call(jq, options.title,panels.length-1);// 返回添加的panel的title给onAdd事件作为参数，并响应事件
		if (options.selected) {
			selectByWhich(jq, panels.length-1);
		}
	}
	;
	/**
	 * 删除指定which的panel
	 * 
	 * @param {Object}
	 *            jq
	 * @param {Object}
	 *            which(index 或者title)
	 * @return {TypeName} void
	 */
	function removeByWhich(jq, which) {
		var options = $.data(jq, "accordion").options;
		var panels = $.data(jq, "accordion").panels;
		stopAll(jq);		
		var panel = getPanelByWhich(jq, which);
		var title = panel.panel("options").title;
		var index = getPanelIndex(jq, panel);				
		// 删除前判断onBeforeRemove的返回结果
		if (options.onBeforeRemove.call(jq, title,index) == false) {
			return;// 若onBeforeRemove返回false则取消删除
		}
		var panel = getPanelByWhich(jq, which, true);
		if (panel) {
			panel.panel("destroy");// 销毁panel
			if (panels.length) {
				resize(jq);
				var selectedPanel = getSelectedPanel(jq);
				if (!selectedPanel) {
					// 若当前没有panel被选中，则选中第一个panel
					selectByWhich(jq, 0);
				}
			}
		}
		options.onRemove.call(jq, title,index);// 返回要删除的title给onRemove事件作为参数，并响应事件
	}

    /**
     *
     * 根据URL，加载菜单信息（add by panwb）
     *
     * @param jq
     * @param url
     */
    function loadMenus(jq, opts) {
        $.ajax({
            type : "post",
            url : opts.url,
            dataType : "json",
            success : function(data) {
                if(!(data instanceof Object)) {
                    data = $.parseJSON(data);
                }
                $(data).each(function() {
                    var content = "<ul class=\"tree tree-lines\">";
                    $(this.content).each(function () {
                        content += "<li><div class=\"tree-node\" style=\"cursor: pointer;\"><span class=\"tree-indent tree-join\"></span>";
                        content += "<img src=\"" + _ContextPath + this.iconUrl + "\"/>";
                        content += "<span class=\"tree-title\">" + this.menuName + "</span>";
                        content += "<span class=\"hide tree-url\">" + _ContextPath + this.menuUrl + "</span>";
                        content += "<span class=\"hide tree-menuId\">" + this.menuId + "</span>";
                        content += "</div></li>";
                    });
                    content += "</ul>";
                    var options = {
                        'title' : this.title,
                        'selected' : false,
                        'content' : content
                    };
                    jq.each(function() {
                        add(this, options);
                    });
                });
                if(opts.success) {
                    opts.success();
                }
            }
        });
    }

	;
	/**
	 * 折叠面板实例化或方法调用
	 * 
	 * @param {Object}
	 *            options 若为string则是方法调用，否则实例化组件
	 * @param {Object}
	 *            param 方法参数
	 * @memberOf {TypeName}
	 * @return {TypeName}
	 */
	$.fn.accordion = function(options, param) {
		if (typeof options == "string") {
			return $.fn.accordion.methods[options](this, param);// 调用相应的方法
		}
		options = options || {};
		return this.each(function() {
			var data = $.data(this, "accordion");
			var opts;
			if (data) {
				opts = $.extend(data.options, options);
				data.opts = opts;
			} else {
				opts = $.extend({}, $.fn.accordion.defaults, $.fn.accordion
						.parseOptions(this), options);
				var r = renderAccordion(this);// 渲染折叠面板
				$.data(this, "accordion", {
					options : opts,
					accordion : r.accordion,
					panels : r.panels
				});
			}
			removeBorderClass(this);
			resize(this);
			selectByTitle(this);
		});
	};
	/**
	 * 方法注册
	 */
	$.fn.accordion.methods = {
		options : function(jq) {
			return $.data(jq[0], "accordion").options;
		},
		panels : function(jq) {
			return $.data(jq[0], "accordion").panels;
		},
		resize : function(jq) {
			return jq.each(function() {
				resize(this);
			});
		},
		getSelected : function(jq) {
			return getSelectedPanel(jq[0]);
		},
		getPanel : function(jq, which) {
			return getPanelByWhich(jq[0], which);
		},
		//add since 1.3
		getPanelIndex : function(jq, panel) {
			return getPanelIndex(jq[0], panel);
		},
		select : function(jq, which) {
			return jq.each(function() {
				selectByWhich(this, which);
			});
		},
		add : function(jq, options) {
			return jq.each(function() {
				add(this, options);
			});
		},
		remove : function(jq, which) {
			return jq.each(function() {
				removeByWhich(this, which);
			});
		},
        loadMenus : function(jq, opts) {
            return loadMenus(jq, opts);
        }
	};
	/**
	 * class声明式定义属性转化为options
	 * 
	 * @param {Object}
	 *            target DOM对象
	 * @return {TypeName}
	 */
	$.fn.accordion.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [ "width", "height", {
			fit : "boolean",
			border : "boolean",
			animate : "boolean"
		} ]));
	};
	/**
	 * 默认属性
	 * 
	 * @param {Object}
	 *            title
	 */
	$.fn.accordion.defaults = {
		width : "auto",
		height : "auto",
		fit : false,
		border : true,
		animate : true,
		onSelect : function(title, index) {
		},
		onAdd : function(title, index) {
		},
		onBeforeRemove : function(title, index) {
		},
		onRemove : function(title, index) {
		}

	};
})(jQuery);