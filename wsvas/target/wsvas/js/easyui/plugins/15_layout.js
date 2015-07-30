(function($) {
	var resizing = false;
	function setSize(container) {
		var opts = $.data(container, "layout").options;
		var panels = $.data(container, "layout").panels;
		var cc = $(container);
		if (opts.fit == true) {
			var p = cc.parent();
			p.addClass("panel-noscroll");
			if (p[0].tagName == "BODY") {
				$("html").addClass("panel-fit");
			}
			cc.width(p.width());
			cc.height(p.height());
		}
		var cpos = {
			top : 0,
			left : 0,
			width : cc.width(),
			height : cc.height()
		};
		function setNorthSize(pp) {
			if (pp.length == 0) {
				return;
			}
			pp.panel("resize", {
				width : cc.width(),
				height : pp.panel("options").height,
				left : 0,
				top : 0
			});
			cpos.top += pp.panel("options").height;
			cpos.height -= pp.panel("options").height;
		}
		;
		if (isVisible(panels.expandNorth)) {
			setNorthSize(panels.expandNorth);
		} else {
			setNorthSize(panels.north);
		}
		function setSouthSize(pp) {
			if (pp.length == 0) {
				return;
			}
			pp.panel("resize", {
				width : cc.width(),
				height : pp.panel("options").height,
				left : 0,
				top : cc.height() - pp.panel("options").height
			});
			cpos.height -= pp.panel("options").height;
		}
		;
		if (isVisible(panels.expandSouth)) {
			setSouthSize(panels.expandSouth);
		} else {
			setSouthSize(panels.south);
		}
		function setEastSize(pp) {
			if (pp.length == 0) {
				return;
			}
			pp.panel("resize", {
				width : pp.panel("options").width,
				height : cpos.height,
				left : cc.width() - pp.panel("options").width,
				top : cpos.top
			});
			cpos.width -= pp.panel("options").width;
		}
		;
		if (isVisible(panels.expandEast)) {
			setEastSize(panels.expandEast);
		} else {
			setEastSize(panels.east);
		}
		function setWestSize(pp) {
			if (pp.length == 0) {
				return;
			}
            pp.panel("resize", {
                width : pp.panel("options").width,
                height : cpos.height,
                left : 0,
                top : cpos.top
            });
            if($.browser.msie) {
                cpos.left += pp.panel("options").width + 5;
                cpos.width -= pp.panel("options").width + 5;
            } else {
                cpos.left += pp.panel("options").width;
                cpos.width -= pp.panel("options").width;
            }

		}
		;
		if (isVisible(panels.expandWest)) {
			setWestSize(panels.expandWest);
		} else {
			setWestSize(panels.west);
		}
		panels.center.panel("resize", cpos);
	}
	;
	function init(jq) {
		var cc = $(jq);
		if (cc[0].tagName == "BODY") {
			$("html").addClass("panel-fit");
		}
		cc.addClass("layout");
		function setRegion(cc) {
			cc.children("div").each(function() {
				var options = $.parser.parseOptions(this, [ "region" ]);
				var r = options.region;
				if (r == "north" || r == "south" || r == "east" || r == "west" || r == "center") {
					addPanel(jq, {
						region : r
					}, this);
				}
			});
		}
		;
		cc.children("form").length ? setRegion(cc.children("form")) : setRegion(cc);
		$("<div class=\"layout-split-proxy-h\"></div>").appendTo(cc);
		$("<div class=\"layout-split-proxy-v\"></div>").appendTo(cc);
		cc.bind("_resize", function(e, fit) {
			var options = $.data(jq, "layout").options;
			if (options.fit == true || fit) {
				setSize(jq);
			}
			return false;
		});
	}
	;
	function addPanel(jq, options, el) {
		options.region = options.region || "center";
		var panels = $.data(jq, "layout").panels;
		var cc = $(jq);
		var dir = options.region;
		if (panels[dir].length) {
			return;
			}
		var pp = $(el);
		if (!pp.length) {
			pp = $("<div></div>").appendTo(cc);
			}
			pp.panel($.extend({}, {
				width : (pp.length ? parseInt(pp[0].style.width) || pp.outerWidth() : "auto"),
				height : (pp.length ? parseInt(pp[0].style.height) || pp.outerHeight() : "auto"),
				split : (pp.attr("split") ? pp.attr("split") == "true" : undefined),
				doSize : false,
				cls : ("layout-panel layout-panel-" + dir),
				bodyCls : "layout-body",
				onOpen : function() {
					var _16 = {
						north : "up",
						south : "down",
						east : "right",
						west : "left"
					};
					if (!_16[dir]) {
						return;
					}
					var btn = "layout-button-" + _16[dir];
					var panelTool = $(this).panel("header").children("div.panel-tool");
					if (!panelTool.children("a." + btn).length) {
						var t = $("<a href=\"javascript:void(0)\"></a>").addClass(btn).appendTo(panelTool);
						t.bind("click", {
							dir : dir
						}, function(e) {
							collapsePanel(jq, e.data.dir);
							return false;
						});
					}
			}
		}, options));
		panels[dir] = pp;
			if (pp.panel("options").split) {
				var panel = pp.panel("panel");
				panel.addClass("layout-split-" + dir);
				var handles = "";
				if (dir == "north") {
					handles = "s";
				}
				if (dir == "south") {
					handles = "n";
				}
				if (dir == "east") {
					handles = "w";
				}
				if (dir == "west") {
					handles = "e";
				}
				panel.resizable({
					handles : handles,
					onStartResize : function(e) {
						resizing = true;
						if (dir == "north" || dir == "south") {
							var proxy = $(">div.layout-split-proxy-v", jq);
						} else {
							var proxy = $(">div.layout-split-proxy-h", jq);
						}
						var top = 0, left = 0, width = 0, height = 0;
						var pos = {
							display : "block"
						};
						if (dir == "north") {
							pos.top = parseInt(panel.css("top")) + panel.outerHeight() - proxy.height();
							pos.left = parseInt(panel.css("left"));
							pos.width = panel.outerWidth();
							pos.height = proxy.height();
						} else {
							if (dir == "south") {
								pos.top = parseInt(panel.css("top"));
								pos.left = parseInt(panel.css("left"));
								pos.width = panel.outerWidth();
								pos.height = proxy.height();
							} else {
								if (dir == "east") {
									pos.top = parseInt(panel.css("top")) || 0;
									pos.left = parseInt(panel.css("left")) || 0;
									pos.width = proxy.width();
									pos.height = panel.outerHeight();
								} else {
									if (dir == "west") {
										pos.top = parseInt(panel.css("top")) || 0;
										pos.left = panel.outerWidth() - proxy.width();
										pos.width = proxy.width();
										pos.height = panel.outerHeight();
									}
								}
							}
						}
						proxy.css(pos);
						$("<div class=\"layout-mask\"></div>").css({
							left : 0,
							top : 0,
							width : cc.width(),
							height : cc.height()
						}).appendTo(cc);
					},
					onResize : function(e) {
						if (dir == "north" || dir == "south") {
							var proxy = $(">div.layout-split-proxy-v", jq);
							proxy.css("top", e.pageY - $(jq).offset().top - proxy.height() / 2);
						} else {
							var proxy = $(">div.layout-split-proxy-h", jq);
							proxy.css("left", e.pageX - $(jq).offset().left - proxy.width() / 2);
						}
						return false;
					},
					onStopResize : function() {
						$(">div.layout-split-proxy-v", jq).css("display", "none");
						$(">div.layout-split-proxy-h", jq).css("display", "none");
						var opts = pp.panel("options");
						opts.width = panel.outerWidth();
						opts.height = panel.outerHeight();
						opts.left = panel.css("left");
						opts.top = panel.css("top");
						pp.panel("resize");
						setSize(jq);
						resizing = false;
						cc.find(">div.layout-mask").remove();
					}
				});
			}
	}
	;
	function removePanel(jq, region) {
		var panels = $.data(jq, "layout").panels;
		if (panels[region].length) {
			panels[region].panel("destroy");
			panels[region] = $();
			var expand = "expand" + region.substring(0, 1).toUpperCase() + region.substring(1);
			if (panels[expand]) {
				panels[expand].panel("destroy");
				panels[expand] = undefined;
			}
		}
	}
	;
	function collapsePanel(container, region, _29) {
		if (_29 == undefined) {
			_29 = "normal";
		}
		var panels = $.data(container, "layout").panels;
		var p = panels[region];
		if (p.panel("options").onBeforeCollapse.call(p) == false) {
			return;
		}
		var pp = "expand" + region.substring(0, 1).toUpperCase() + region.substring(1);
		if (!panels[pp]) {
			panels[pp] = createExpandPanel(region);
			panels[pp].panel("panel").click(function() {
				var p1 = createPanel();
				p.panel("expand", false).panel("open").panel("resize", p1.collapse);
				p.panel("panel").animate(p1.expand);
				return false;
			});
		}
		var p2 = createPanel();
		if (!isVisible(panels[pp])) {
			panels.center.panel("resize", p2.resizeC);
		}

        //修改效果
	    p.panel("panel").animate(p2.collapse, _29, function() {
			p.panel("collapse", false).panel("close");
			panels[pp].panel("open").panel("resize", p2.expandP);
		});

		function createExpandPanel(dir) {
			var icon;
			if (dir == "east") {
				icon = "layout-button-left";
			} else {
				if (dir == "west") {
					icon = "layout-button-right";
				} else {
					if (dir == "north") {
						icon = "layout-button-down";
					} else {
						if (dir == "south") {
							icon = "layout-button-up";
						}
					}
				}
			}
			var p = $("<div></div>").appendTo(container).panel({
				cls : "layout-expand",
				title : "&nbsp;",
				closed : true,
				doSize : false,
				tools : [ {
					iconCls : icon,
					handler : function() {
						expandPanel(container, region);
						return false;
					}
				} ]
			});
			p.panel("panel").hover(function() {
				$(this).addClass("layout-expand-over");
			}, function() {
				$(this).removeClass("layout-expand-over");
			});
			return p;
		}
		;
		function createPanel() {
			var cc = $(container);
			if (region == "east") {
				return {
					resizeC : {
						width : panels.center.panel("options").width + panels["east"].panel("options").width - 28
					},
					expand : {
						left : cc.width() - panels["east"].panel("options").width
					},
					expandP : {
						top : panels["east"].panel("options").top,
						left : cc.width() - 28,
						width : 28,
						height : panels["center"].panel("options").height
					},
					collapse : {
						left : cc.width()
					}
				};
			} else {
				if (region == "west") {
					return {
						resizeC : {
							width : panels.center.panel("options").width + panels["west"].panel("options").width - 28,
							left : 28
						},
						expand : {
							left : 0
						},
						expandP : {
							left : 0,
							top : panels["west"].panel("options").top,
							width : 28,
							height : panels["center"].panel("options").height
						},
						collapse : {
							left : -panels["west"].panel("options").width
						}
					};
				} else {
					if (region == "north") {
						var hh = cc.height() - 28;
						if (isVisible(panels.expandSouth)) {
							hh -= panels.expandSouth.panel("options").height;
						} else {
							if (isVisible(panels.south)) {
								hh -= panels.south.panel("options").height;
							}
						}
						panels.east.panel("resize", {
							top : 28,
							height : hh
						});
						panels.west.panel("resize", {
							top : 28,
							height : hh
						});
							if (isVisible(panels.expandEast)) {
								panels.expandEast.panel("resize", {
								top : 28,
								height : hh
							});
						}
						if (isVisible(panels.expandWest)) {
							panels.expandWest.panel("resize", {
								top : 28,
								height : hh
							});
						}
						return {
							resizeC : {
								top : 28,
								height : hh
							},
							expand : {
								top : 0
							},
							expandP : {
								top : 0,
								left : 0,
								width : cc.width(),
								height : 28
							},
							collapse : {
								top : -panels["north"].panel("options").height
							}
						};
					} else {
						if (region == "south") {
							var hh = cc.height() - 28;
							if (isVisible(panels.expandNorth)) {
								hh -= panels.expandNorth.panel("options").height;
							} else {
								if (isVisible(panels.north)) {
									hh -= panels.north.panel("options").height;
								}
							}
							panels.east.panel("resize", {
								height : hh
							});
							panels.west.panel("resize", {
								height : hh
							});
							if (isVisible(panels.expandEast)) {
								panels.expandEast.panel("resize", {
									height : hh
								});
							}
							if (isVisible(panels.expandWest)) {
								panels.expandWest.panel("resize", {
									height : hh
								});
							}
							return {
								resizeC : {
									height : hh
								},
								expand : {
									top : cc.height() - panels["south"].panel("options").height
								},
								expandP : {
									top : cc.height() - 28,
									left : 0,
									width : cc.width(),
									height : 28
								},
								collapse : {
									top : cc.height()
								}
							};
						}
					}
				}
			}
		}
		;
	}
	;
	function expandPanel(container, region) {
		var panels = $.data(container, "layout").panels;
		var cc = expandResion();
		var p = panels[region];
		if (p.panel("options").onBeforeExpand.call(p) == false) {
				return;
		}
		var closeRegion = "expand" + region.substring(0, 1).toUpperCase() + region.substring(1);
		panels[closeRegion].panel("close");
		p.panel("panel").stop(true, true);
		p.panel("expand", false).panel("open").panel("resize", cc.collapse);
		p.panel("panel").animate(cc.expand, function() {
			setSize(container);
		});
		function expandResion() {
			var cc = $(container);
			if (region == "east" && panels.expandEast) {
				return {
					collapse : {
						left : cc.width()
					},
					expand : {
						left : cc.width() - panels["east"].panel("options").width
					}
				};
			} else {
				if (region == "west" && panels.expandWest) {
					return {
						collapse : {
							left : -panels["west"].panel("options").width
						},
						expand : {
							left : 0
						}
					};
				} else {
					if (region == "north" && panels.expandNorth) {
						return {
							collapse : {
								top : -panels["north"].panel("options").height
							},
							expand : {
								top : 0
							}
						};
					} else {
						if (region == "south" && panels.expandSouth) {
							return {
								collapse : {
									top : cc.height()
								},
								expand : {
									top : cc.height() - panels["south"].panel("options").height
								}
							};
						}
					}
				}
			}
		}
	}
	;
	function bindEvents(container) {
		var panels = $.data(container, "layout").panels;
		var cc = $(container);
		if (panels.east.length) {
			panels.east.panel("panel").bind("mouseover", "east", setCollapsePanel);
		}
		if (panels.west.length) {
			panels.west.panel("panel").bind("mouseover", "west", setCollapsePanel);
		}
		if (panels.north.length) {
			panels.north.panel("panel").bind("mouseover", "north", setCollapsePanel);
		}
		if (panels.south.length) {
			panels.south.panel("panel").bind("mouseover", "south", setCollapsePanel);
		}
		panels.center.panel("panel").bind("mouseover", "center", setCollapsePanel);
		function setCollapsePanel(e) {
			if (resizing == true) {
				return;
			}
			if (e.data != "east" && isVisible(panels.east) && isVisible(panels.expandEast)) {
				collapsePanel(container, "east");
			}
			if (e.data != "west" && isVisible(panels.west) && isVisible(panels.expandWest)) {
				collapsePanel(container, "west");
			}
			if (e.data != "north" && isVisible(panels.north) && isVisible(panels.expandNorth)) {
				collapsePanel(container, "north");
			}
			if (e.data != "south" && isVisible(panels.south) && isVisible(panels.expandSouth)) {
				collapsePanel(container, "south");
			}
			return false;
		}
		;
	}
	;
	function isVisible(pp) {
		if (!pp) {
			return false;
		}
		if (pp.length) {
			return pp.panel("panel").is(":visible");
		} else {
			return false;
		}
	}
	;
	function setCollapsedPanel(jq) {
		var panels = $.data(jq, "layout").panels;
		if (panels.east.length && panels.east.panel("options").collapsed) {
			collapsePanel(jq, "east", 0);
		}
		if (panels.west.length && panels.west.panel("options").collapsed) {
			collapsePanel(jq, "west", 0);
		}
		if (panels.north.length && panels.north.panel("options").collapsed) {
			collapsePanel(jq, "north", 0);
		}
		if (panels.south.length && panels.south.panel("options").collapsed) {
			collapsePanel(jq, "south", 0);
		}
	}
	;
	$.fn.layout = function(options, param) {
		if (typeof options == "string") {
			return $.fn.layout.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "layout");
			if (state) {
				$.extend(state.options, options);
			} else {
				var opts = $.extend({}, $.fn.layout.defaults, $.fn.layout.parseOptions(this), options);
				$.data(this, "layout", {
					options : opts,
					panels : {
						center : $(),
						north : $(),
						south : $(),
						east : $(),
						west : $()
					}
				});
				init(this);
				bindEvents(this);				
			}
			setSize(this);
			setCollapsedPanel(this);
		});
	};
	$.fn.layout.methods = {
		resize : function(jq) {
			return jq.each(function() {
				setSize(this);
			});
		},
		panel : function(jq, region) {
			return $.data(jq[0], "layout").panels[region];
		},
		collapse : function(jq, region) {
			return jq.each(function() {
				collapsePanel(this, region);
			});
		},
		expand : function(jq, region) {
			return jq.each(function() {
				expandPanel(this, region);
			});
		},
		add : function(jq, options) {
			return jq.each(function() {
				addPanel(this, options);
				setSize(this);
				if ($(this).layout("panel", options.region).panel("options").collapsed) {
					collapsePanel(this, options.region, 0);
				}
			});
		},
		remove : function(jq, region) {
			return jq.each(function() {
				removePanel(this, region);
				setSize(this);
			});
		}
	};
	$.fn.layout.parseOptions = function(target) {
		return $.extend({}, $.parser.parseOptions(target, [ {
			fit : "boolean"
		} ]));
	};
	$.fn.layout.defaults = {
		fit : false
	};
})(jQuery);
