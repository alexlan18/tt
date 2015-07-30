(function($) {
	var _1 = 0;
	//获取值在数组中的索引
	function indexOf(a, o) {
		for ( var i = 0, len = a.length; i < len; i++) {
			if (a[i] == o) {
				return i;
			}
		}
		return -1;
	}
	;
	//删除数组中对应的数据
	function removeById(a, o, id) {
		if (typeof o == "string") {
			for ( var i = 0, len = a.length; i < len; i++) {
				if (a[i][o] == id) {
					a.splice(i, 1);
					return;
				}
			}
		} else {
			var index = indexOf(a, o);
			if (index != -1) {
				a.splice(index, 1);
			}
		}
	}
	;
	//设置大小
	function setSize(target, param) {
		var opts = $.data(target, "datagrid").options;
		var panel = $.data(target, "datagrid").panel;
		if (param) {
			if (param.width) {
				opts.width = param.width;
			}
			if (param.height) {
				opts.height = param.height;
			}
		}
		if (opts.fit == true) {
			var p = panel.panel("panel").parent();
			opts.width = p.width();
			opts.height = p.height();
		}
		panel.panel("resize", {
			width : opts.width,
			height : opts.height
		});
	}
	;
	function fitGridSize(target) {
		var opts = $.data(target, "datagrid").options;
		var dc = $.data(target, "datagrid").dc;
		var wrap = $.data(target, "datagrid").panel;
		var width = wrap.width();
		var height = wrap.height();
		var view = dc.view;
		var gridView1 = dc.view1;
		var gridView2 = dc.view2;
		var gridHeader1 = gridView1.children("div.datagrid-header");
		var gridHeader2 = gridView2.children("div.datagrid-header");
		var gridTable1 = gridHeader1.find("table");
		var gridTable2 = gridHeader2.find("table");
		view.width(width);
		var innerHeader = gridHeader1.children("div.datagrid-header-inner").show();
		gridView1.width(innerHeader.find("table").width());
		if (!opts.showHeader) {
			innerHeader.hide();
		}
		gridView2.width(width - gridView1.outerWidth());
		gridView1.children("div.datagrid-header,div.datagrid-body,div.datagrid-footer").width(gridView1.width());
		gridView2.children("div.datagrid-header,div.datagrid-body,div.datagrid-footer").width(gridView2.width());
		var hh;
		gridHeader1.css("height", "");
		gridHeader2.css("height", "");
		gridTable1.css("height", "");
		gridTable2.css("height", "");
		hh = Math.max(gridTable1.height(), gridTable2.height());
		gridTable1.height(hh);
		gridTable2.height(hh);
		gridHeader1.add(gridHeader2)._outerHeight(hh);
		if (opts.height != "auto") {
			var fixedHeight = height - gridView2.children("div.datagrid-header").outerHeight(true) - gridView2.children("div.datagrid-footer").outerHeight(true) - wrap.children("div.datagrid-toolbar").outerHeight(true);
			wrap.children("div.datagrid-pager").each(function() {
				fixedHeight -= $(this).outerHeight(true);
			});
			gridView1.children("div.datagrid-body").height(fixedHeight);
			gridView2.children("div.datagrid-body").height(fixedHeight);
		}
		view.height(gridView2.height());
		gridView2.css("left", gridView1.outerWidth());
	}
	;
	function fixRowHeight(target, rowIndex, autoRowHeight) {
		var rows = $.data(target, "datagrid").data.rows;
		var opts = $.data(target, "datagrid").options;
		var dc = $.data(target, "datagrid").dc;
		if (!dc.body1.is(":empty") && (!opts.nowrap || opts.autoRowHeight || autoRowHeight)) {
			if (rowIndex != undefined) {
				var tr1 = opts.finder.getTr(target, rowIndex, "body", 1);
				var tr2 = opts.finder.getTr(target, rowIndex, "body", 2);
				alignRowHeight(tr1,tr2);
			} else {
				var tr1 = opts.finder.getTr(target, 0, "allbody", 1);
				var tr2 = opts.finder.getTr(target, 0, "allbody", 2);
				alignRowHeight(tr1, tr2);
				if (opts.showFooter) {
					var tr1 = opts.finder.getTr(target, 0, "allfooter", 1);
					var tr2 = opts.finder.getTr(target, 0, "allfooter", 2);
					alignRowHeight(tr1, tr2);
				}
			}
		}
		fitGridSize(target);
		if (opts.height == "auto") {
			var gridBody1 = dc.body1.parent();
			var gridBody2 = dc.body2;
			var fullHeight = 0;
			var width = 0;
			gridBody2.children().each(function() {
				var c = $(this);
				if (c.is(":visible")) {
					fullHeight += c.outerHeight();
					if (width < c.outerWidth()) {
						width = c.outerWidth();
					}
				}
			});
			if (width > gridBody2.width()) {
				fullHeight += 18;
			}
			gridBody1.height(fullHeight);
			gridBody2.height(fullHeight);
			dc.view.height(dc.view2.height());
		}
		dc.body2.triggerHandler("scroll");
		function alignRowHeight(_26, _27) {
			for ( var i = 0; i < _27.length; i++) {
				var tr1 = $(_26[i]);
				var tr2 = $(_27[i]);
				tr1.css("height", "");
				tr2.css("height", "");
				var height = Math.max(tr1.height(), tr2.height());
				tr1.css("height", height);
				tr2.css("height", height);
			}
		}
		;
	}
	;
	//重塑Grid
	function wrapGrid(target, rownumbers) {
		function getColumns() {
			var frozenColumns = [];
			var columns = []; 
			$(target).children("thead").each(function() {
				var opt = $.parser.parseOptions(this, [ {
					frozen : "boolean"
				} ]);
				$(this).find("tr").each(function() {
					var cols = [];
					$(this).find("th").each(function() {
						var th = $(this);
						var col = $.extend({}, $.parser.parseOptions(this, [ "field", "align", "dropdown", {
							sortable : "boolean",
							checkbox : "boolean",
							resizable : "boolean"
						}, {
							rowspan : "number",
							colspan : "number",
							width : "number"
						} ]), {
							title : (th.html() || undefined),
							hidden : (th.attr("hidden") ? true : undefined),
							formatter : (th.attr("formatter") ? eval(th.attr("formatter")) : undefined),
							styler : (th.attr("styler") ? eval(th.attr("styler")) : undefined)
						});
						if (!col.align) {
							col.align = "left";
						}
					if (th.attr("editor")) {
						var s = $.trim(th.attr("editor"));
						if (s.substr(0, 1) == "{") {
							col.editor = eval("(" + s + ")");
						} else {
							col.editor = s;
						}
					}
					cols.push(col);
				});
				opt.frozen ? frozenColumns.push(cols) : columns.push(cols);
				});
			});
			return [frozenColumns,columns];
		}
		;
		var wrap = $(
				"<div class=\"datagrid-wrap\">" 
				 	+ "<div class=\"datagrid-view\">" 
				  		+ "<div class=\"datagrid-view1\">" 
				  			+ "<div class=\"datagrid-header\">" 
				  				+ "<div class=\"datagrid-header-inner\"></div>" 
				  			+ "</div>" 
				  			+ "<div class=\"datagrid-body\">"
				  				+ "<div class=\"datagrid-body-inner\"></div>" 
				  			+ "</div>" 
				  			+ "<div class=\"datagrid-footer\">" 
				  				+ "<div class=\"datagrid-footer-inner\"></div>" 
				  			+ "</div>" 
				  		+ "</div>" 
				  		+ "<div class=\"datagrid-view2\">"
				  			+ "<div class=\"datagrid-header\">" 
				  				+ "<div class=\"datagrid-header-inner\"></div>" 
				  			+ "</div>" 
				  			+ "<div class=\"datagrid-body\"></div>" 
				  			+ "<div class=\"datagrid-footer\">" 
				  				+ "<div class=\"datagrid-footer-inner\"></div>"
				  			+ "</div>" 
				  		+ "</div>" 
				  	+ "</div>" 
				+ "</div>").insertAfter(target);
		wrap.panel({
			doSize : false
		});
		wrap.panel("panel").addClass("datagrid").bind("_resize", function(e, param) {
			var opts = $.data(target, "datagrid").options;
			if (opts.fit == true || param) {
				setSize(target);
				setTimeout(function() {
					if ($.data(target, "datagrid")) {
						fixColumnSize(target);
					}
				}, 0);
			}
			return false;
		});
		$(target).hide().appendTo(wrap.children("div.datagrid-view"));
		var columns = getColumns();
		var view = wrap.children("div.datagrid-view");
		var gridView1 = view.children("div.datagrid-view1");
		var gridView2 = view.children("div.datagrid-view2");
		return {
			panel : wrap,
			frozenColumns : columns[0],
			columns : columns[1],
			dc : {
				view : view,
				view1 : gridView1,
				view2 : gridView2,
				header1 : gridView1.children("div.datagrid-header").children("div.datagrid-header-inner"),
				header2 : gridView2.children("div.datagrid-header").children("div.datagrid-header-inner"),
				body1 : gridView1.children("div.datagrid-body").children("div.datagrid-body-inner"),
				body2 : gridView2.children("div.datagrid-body"),
				footer1 : gridView1.children("div.datagrid-footer").children("div.datagrid-footer-inner"),
				footer2 : gridView2.children("div.datagrid-footer").children("div.datagrid-footer-inner")
			}
		};
	}
	;
	//获取表格数据
	function getGridData(target) {
		var data = {
			total : 0,
			rows : []
		};
		var fields = getColumnFields(target, true).concat(getColumnFields(target, false));
		$(target).find("tbody tr").each(function() {
			data.total++;
			var col = {};
			for ( var i = 0; i < fields.length; i++) {
				col[fields[i]] = $("td:eq(" + i + ")", this).html();
			}
			data.rows.push(col);
		});
		return data;
	}
	;
	//初始化表格
	function init(target) {
		var dg = $.data(target, "datagrid");
		var opts = dg.options;
		var dc = dg.dc;
		var panel = dg.panel;
		panel.panel($.extend({}, opts, {
			id : null,
			doSize : false,
			onResize : function(width, height) {
				setTimeout(function() {
					if ($.data(target, "datagrid")) {
						fitGridSize(target);
						fitColumns(target);
						opts.onResize.call(panel, width, height);
					}
				}, 0);
			},
			onExpand : function() {
				fixRowHeight(target);
				opts.onExpand.call(panel);
			}
		}));
		dg.rowIdPrefix = "datagrid-row-r" + (++_1);
		buildGridHeader(dc.header1, opts.frozenColumns, true);
		buildGridHeader(dc.header2, opts.columns, false);
		_44();
		dc.header1.add(dc.header2).css("display", opts.showHeader ? "block" : "none");
		dc.footer1.add(dc.footer2).css("display", opts.showFooter ? "block" : "none");
		if (opts.toolbar) {
			if (typeof opts.toolbar == "string") {
				$(opts.toolbar).addClass("datagrid-toolbar").prependTo(panel);
				$(opts.toolbar).show();
			} else {
				$("div.datagrid-toolbar", panel).remove();
				var tb = $("<div class=\"datagrid-toolbar\"></div>").prependTo(panel);
				for ( var i = 0; i < opts.toolbar.length; i++) {
					var btn = opts.toolbar[i];
					if (btn == "-") {
						$("<div class=\"datagrid-btn-separator\"></div>").appendTo(tb);
					} else {
						var tool = $("<a href=\"javascript:void(0)\"></a>");
						tool[0].onclick = eval(btn.handler || function() {
						});
						tool.css("float", "left").appendTo(tb).linkbutton($.extend({}, btn, {
							plain : true
						}));
					}
				}
			}
		} else {
			$("div.datagrid-toolbar", panel).remove();
		}
		$("div.datagrid-pager", panel).remove();
		if (opts.pagination) {
			var pager = $("<div class=\"datagrid-pager\"></div>");
			if (opts.pagePosition == "bottom") {
				pager.appendTo(panel);
			} else {
				if (opts.pagePosition == "top") {
					pager.addClass("datagrid-pager-top").prependTo(panel);
				} else {
					var _47 = $("<div class=\"datagrid-pager datagrid-pager-top\"></div>").prependTo(panel);
					pager.appendTo(panel);
					pager = pager.add(_47);
				}
			}
			pager.pagination({
				pageNumber : opts.pageNumber,
				pageSize : opts.pageSize,
				pageList : opts.pageList,
				onSelectPage : function(pageNumber, pageSize) {
					opts.pageNumber = pageNumber;
					opts.pageSize = pageSize;
					pager.pagination("refresh", {
						pageNumber : pageNumber,
						pageSize : pageSize
					});
					//modify by zhengshj 2012-08-08
					if(opts.command){
						$("#"+opts.command).command("execute");
					} else {
						request(target);
					}
				}
			});
			opts.pageSize = pager.pagination("options").pageSize;
		}
		function buildGridHeader(request, columns, frozen) {
			if (!columns) {
				return;
			}
			$(request).show();
			$(request).empty();
			var t = $("<table class=\"datagrid-htable\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tbody></tbody></table>").appendTo(request);
			for ( var i = 0; i < columns.length; i++) {
				var tr = $("<tr class=\"datagrid-header-row\"></tr>").appendTo($("tbody", t));
				var cols = columns[i];
				for ( var j = 0; j < cols.length; j++) {
					var col = cols[j];
					var attr = "";
					if (col.rowspan) {
						attr += "rowspan=\"" + col.rowspan + "\" ";
					}
					if (col.colspan) {
						attr += "colspan=\"" + col.colspan + "\" ";
					}
					var td = $("<td " + attr + "></td>").appendTo(tr);
					if (col.checkbox) {
						td.attr("field", col.field);
						$("<div class=\"datagrid-header-check\"></div>").html("<input type=\"checkbox\"/>").appendTo(td);
					} else {
						if (col.field) {
							td.attr("field", col.field);
							td.append("<div class=\"datagrid-cell\"><span></span><span class=\"datagrid-sort-icon\"></span></div>");
							$("span", td).html(col.title);
							$("span.datagrid-sort-icon", td).html("&nbsp;");
							var cell = td.find("div.datagrid-cell");
							if (col.resizable == false) {
								cell.attr("resizable", "false");
							}
							if (col.width) {
								cell._outerWidth(col.width);
								col.boxWidth = parseInt(cell[0].style.width);
							} else {
								col.auto = true;
							}
							cell.css("text-align", (col.align || "left"));
							col.cellClass = "datagrid-cell-c" + _1 + "-" + col.field.replace(".", "-");
							col.cellSelector = "div." + col.cellClass;
						} else {
							$("<div class=\"datagrid-cell-group\"></div>").html(col.title).appendTo(td);
						}
					}
					if (col.hidden) {
						td.hide();
					}
				}
			}
			
			var td = "";
			if (frozen && opts.rownumbers) {
				td = "<td rowspan=\"" + opts.frozenColumns.length + "\"><div class=\"datagrid-header-rownumber\"></div></td>";
				if (opts.showIndicator) {
					td += "<td><div class=\"datagrid-header-rownumber\"></div></td>";
				}
				if ($("tr", t).length == 0) {
					$("<tr class=\"datagrid-header-row\">"+td+"</tr>").appendTo($("tbody", t));
				} else {
					$(td).prependTo($("tr:first", t));
				}
			}
		}
		;
		function _44() {
			dc.view.children("style").remove();
			var ss = [ "<style type=\"text/css\">" ];
			var columnFields = getColumnFields(target, true).concat(getColumnFields(target));
			for ( var i = 0; i < columnFields.length; i++) {
				var col = getColumnOption(target, columnFields[i]);
				if (col && !col.checkbox) {
					ss.push(col.cellSelector + " {width:" + col.boxWidth + "px;}");
				}
			}
			ss.push("</style>");
			$(ss.join("\n")).prependTo(dc.view);
		}
		;
	}
	;
	
	function setProperties(target) {
		var dg = $.data(target, "datagrid");
		var panel = dg.panel;
		var opts = dg.options;
		var dc = dg.dc;
		var gridHeader = dc.header1.add(dc.header2);
		gridHeader.find("td:has(div.datagrid-cell)").unbind(".datagrid").bind("mouseenter.datagrid", function() {
			$(this).addClass("datagrid-header-over");
		}).bind("mouseleave.datagrid", function() {
			$(this).removeClass("datagrid-header-over");
		}).bind("contextmenu.datagrid", function(e) {
			var field = $(this).attr("field");
			opts.onHeaderContextMenu.call(target, e, field);
		});
		gridHeader.find("input[type=checkbox]").unbind(".datagrid").bind("click.datagrid", function(e) {
			if (opts.singleSelect && opts.selectOnCheck) {
				return false;
			}
			if ($(this).is(":checked")) {
				checkAll(target);
			} else {
				uncheckAll(target);
			}
			e.stopPropagation();
		});
		var cell = gridHeader.find("div.datagrid-cell");
		cell.unbind(".datagrid").bind("click.datagrid", function(e) {
			if (e.pageX < $(this).offset().left + $(this).outerWidth() - 5) {
				var fieldName = $(this).parent().attr("field");
				var col = getColumnOption(target, fieldName);
				if (!col.sortable || dg.resizing) {
					return;
				}
				opts.sortName = fieldName;
				opts.sortOrder = "asc";
				var c = "datagrid-sort-asc";
				if ($(this).hasClass(c)) {
					c = "datagrid-sort-desc";
					opts.sortOrder = "desc";
				}
				cell.removeClass("datagrid-sort-asc datagrid-sort-desc");
				$(this).addClass(c);
				if (opts.remoteSort) {
					request(target);
				} else {
					var data = $.data(target, "datagrid").data;
					renderGrid(target, data);
				}
				opts.onSortColumn.call(target, opts.sortName, opts.sortOrder);
			}
		}).bind("dblclick.datagrid", function(e) {
			if (e.pageX > $(this).offset().left + $(this).outerWidth() - 5) {
				var fieldName = $(this).parent().attr("field");
				var col = getColumnOption(target, fieldName);
				if (col.resizable == false) {
					return;
				}
				$(target).datagrid("autoSizeColumn", fieldName);
				col.auto = false;
			}
		});
		cell.each(function() {
			$(this).resizable({
				handles : "e",
				disabled : ($(this).attr("resizable") ? $(this).attr("resizable") == "false" : false),
				minWidth : 25,
				onStartResize : function(e) {
					dg.resizing = true;
					gridHeader.css("cursor", "e-resize");
					if (!dg.proxy) {
						dg.proxy = $("<div class=\"datagrid-resize-proxy\"></div>").appendTo(dc.view);
					}
					dg.proxy.css({
						left : e.pageX - $(panel).offset().left - 1,
						display : "none"
					});
					setTimeout(function() {
						if (dg.proxy) {
							dg.proxy.show();
						}
					}, 500);
				},
				onResize : function(e) {
					dg.proxy.css({
						left : e.pageX - $(panel).offset().left - 1,
						display : "block"
					});
					return false;
				},
				onStopResize : function(e) {
					gridHeader.css("cursor", "");
					var field = $(this).parent().attr("field");
					var col = getColumnOption(target, field);
					col.width = $(this).outerWidth();
					col.boxWidth = parseInt(this.style.width);
					col.auto = undefined;
					fixColumnSize(target, field);
					dc.view2.children("div.datagrid-header").scrollLeft(dc.body2.scrollLeft());
					dg.proxy.remove();
					dg.proxy = null;
					if ($(this).parents("div:first.datagrid-header").parent().hasClass("datagrid-view1")) {
						fitGridSize(target);
					}
					fitColumns(target);
					opts.onResizeColumn.call(target, field, col.width);
					setTimeout(function() {
						dg.resizing = false;
					}, 0);
				}
			});
		});
		dc.body1.add(dc.body2).unbind().bind("mouseover", function(e) {
			var tr = $(e.target).closest("tr.datagrid-row");
			if (!tr.length) {
				return;
			}
			var rowIndex = getRowIndex(tr);
			opts.finder.getTr(target, rowIndex).addClass("datagrid-row-over");
			e.stopPropagation();
		}).bind("mouseout", function(e) {
			var tr = $(e.target).closest("tr.datagrid-row");
			if (!tr.length) {
				return;
			}
			var rowIndex = getRowIndex(tr);
			opts.finder.getTr(target, rowIndex).removeClass("datagrid-row-over");
			e.stopPropagation();
		}).bind("click", function(e) {
			var tt = $(e.target);
			var tr = tt.closest("tr.datagrid-row");
			if (!tr.length) {
				return;
			}
			var rowIndex = getRowIndex(tr);
			//add by zhengshj 2012-08-01 
			//编辑状态下，先结束上次
			if (opts.editing && opts.editIndex) {
				if(!validateRow(target, opts.editIndex)){
					return;
				}
				stopEdit(target, opts.editIndex);
			}
			if (tt.parent().hasClass("datagrid-cell-check")) {
				if (opts.singleSelect && opts.selectOnCheck) {
					if (!opts.checkOnSelect) {
						uncheckAll(target, true);
					}
					checkRow(target, rowIndex);
				} else {
					if (tt.is(":checked")) {
						checkRow(target, rowIndex);
					} else {
						uncheckRow(target, rowIndex);
					}
				}
			} else {
				var row = opts.finder.getRow(target, rowIndex);
				var td = tt.closest("td[field]", tr);
				if (td.length) {
					var fieldName = td.attr("field");
					opts.onClickCell.call(target, rowIndex, fieldName, row[fieldName]);
				}
				if (opts.singleSelect == true) {
					selectRow(target, rowIndex);
				} else {
					if (tr.hasClass("datagrid-row-selected")) {
						unselectRow(target, rowIndex);
					} else {
						selectRow(target, rowIndex);
					}
				}
				opts.onClickRow.call(target, rowIndex, row);
			}
			e.stopPropagation();
		}).bind("dblclick", function(e) {
			var tt = $(e.target);
			var tr = tt.closest("tr.datagrid-row");
			if (!tr.length) {
				return;
			}
			var rowIndex = getRowIndex(tr);
			var row = opts.finder.getRow(target, rowIndex);
			var td = tt.closest("td[field]", tr);
			if (td.length) {
				var fieldName = td.attr("field");
				//add by zhengshj 2012-08-01 
				// 双击编辑行
				if (opts.editing) {
					if (validateRow(target, opts.editIndex)) {
						stopEdit(target, opts.editIndex);
						beginEdit(target, rowIndex);
						opts.editIndex = rowIndex;

						// 自动选中单元格
						var editor = getEditor(target, {
							index : rowIndex,
							field : fieldName
						});
						if (editor) {
							editor.target.select();
							editor.target.focus();
						} else {
							var editors = getEditors(target, rowIndex);
							if (editors.length) {
								editors[0].target.select();
								editors[0].target.focus();
							}
						}
					} else {
						return;
					}
				}
				opts.onDblClickCell.call(target, rowIndex, fieldName, row[fieldName]);
			}
			opts.onDblClickRow.call(target, rowIndex, row);
			e.stopPropagation();
		}).bind("contextmenu", function(e) {
			var tr = $(e.target).closest("tr.datagrid-row");
			if (!tr.length) {
				return;
			}
			var rowIndex = getRowIndex(tr);
			var row = opts.finder.getRow(target, rowIndex);
			opts.onRowContextMenu.call(target, e, rowIndex, row);
			e.stopPropagation();
		});
		dc.body2.bind("scroll", function() {
			dc.view1.children("div.datagrid-body").scrollTop($(this).scrollTop());
			dc.view2.children("div.datagrid-header,div.datagrid-footer").scrollLeft($(this).scrollLeft());
		});
		function getRowIndex(tr) {
			if (tr.attr("datagrid-row-index")) {
				return parseInt(tr.attr("datagrid-row-index"));
			} else {
				return tr.attr("node-id");
			}
		}
		;
	}
	;
	function fitColumns(target) {
		var opts = $.data(target, "datagrid").options;
		var dc = $.data(target, "datagrid").dc;
		if (!opts.fitColumns) {
			return;
		}
		var gridHeader2 = dc.view2.children("div.datagrid-header");
		var visableWidth = 0;
		var visableColumn;
		var fields = getColumnFields(target, false);
		for ( var i = 0; i < fields.length; i++) {
			var col = getColumnOption(target, fields[i]);
			if (_6d(col)) {
				visableWidth += col.width;
				visableColumn = col;
			}
		}
		var innerHeader = gridHeader2.children("div.datagrid-header-inner").show();
		var fullWidth = gridHeader2.width() - gridHeader2.find("table").width() - opts.scrollbarSize;
		var rate = fullWidth / visableWidth;
		if (!opts.showHeader) {
			innerHeader.hide();
		}
		for ( var i = 0; i < fields.length; i++) {
			var col = getColumnOption(target, fields[i]);
			if (_6d(col)) {
				var columnWidth = Math.floor(col.width * rate);
				fitColumnWidth(col, columnWidth);
				fullWidth -= columnWidth;
			}
		}
		if (visableColumn && fullWidth) {
			fitColumnWidth(visableColumn, fullWidth);
		}
		fixColumnSize(target);
		function fitColumnWidth(col, width) {
			col.width += width;
			col.boxWidth += width;
			gridHeader2.find("td[field=\"" + col.field + "\"] div.datagrid-cell").width(col.boxWidth);
		}
		;
		function _6d(col) {
			if (!col.hidden && !col.checkbox && !col.auto) {
				return true;
			}
		}
		;
	}
	;
	function autoSizeColumn(target, field) {
		var opts = $.data(target, "datagrid").options;
		var dc = $.data(target, "datagrid").dc;
		if (field) {
			_7(field);
			if (opts.fitColumns) {
				fitGridSize(target);
				fitColumns(target);
			}
		} else {
			var _78 = false;
			var columnFields = getColumnFields(target, true).concat(getColumnFields(target, false));
			for ( var i = 0; i < columnFields.length; i++) {
				var field = columnFields[i];
				var col = getColumnOption(target, field);
				if (col.auto) {
					_7(field);
					_78 = true;
				}
			}
			if (_78 && opts.fitColumns) {
				fitGridSize(target);
				fitColumns(target);
			}
		}
		function _7(field) {
			var cell = dc.view.find("div.datagrid-header td[field=\"" + field + "\"] div.datagrid-cell");
			cell.css("width", "");
			var col = $(target).datagrid("getColumnOption", field);
			col.width = undefined;
			col.boxWidth = undefined;
			col.auto = true;
			$(target).datagrid("fixColumnSize", field);
			var width = Math.max(cell.outerWidth(), getColumnWidth("allbody"), getColumnWidth("allfooter"));
			cell._outerWidth(width);
			col.width = width;
			col.boxWidth = parseInt(cell[0].style.width);
			fixColumnSize(target, field);
			opts.onResizeColumn.call(target, field, col.width);
			function getColumnWidth(tag) {
				var width = 0;
				opts.finder.getTr(target, 0, tag).find("td[field=\"" + field + "\"] div.datagrid-cell").each(function() {
					var w = $(this).outerWidth();
					if (width < w) {
						width = w;
					}
				});
				return width;
			}
			;
		}
		;
	}
	;
	function fixColumnSize(target, cell) {
		var opts = $.data(target, "datagrid").options;
		var dc = $.data(target, "datagrid").dc;
		var _83 = dc.view.find("table.datagrid-btable,table.datagrid-ftable");
		_83.css("table-layout", "fixed");
		if (cell) {
			fix(cell);
		} else {
			var ff = getColumnFields(target, true).concat(getColumnFields(target, false));
			for ( var i = 0; i < ff.length; i++) {
				fix(ff[i]);
			}
		}
		_83.css("table-layout", "auto");
		fixMergedCellsSize(target);
		setTimeout(function() {
			fixRowHeight(target);
			fixEditorSize(target);
		}, 0);
		function fix(cell) {
			var col = getColumnOption(target, cell);
			if (col.checkbox) {
				return;
			}
			var style = dc.view.children("style")[0];
			var styleSheet = style.styleSheet ? style.styleSheet : (style.sheet || document.styleSheets[document.styleSheets.length - 1]);
			var cssRules = styleSheet.cssRules || styleSheet.rules;
			for ( var i = 0, len = cssRules.length; i < len; i++) {
				var cssRule = cssRules[i];
				if (cssRule.selectorText.toLowerCase() == col.cellSelector.toLowerCase()) {
					cssRule.style["width"] = col.boxWidth ? col.boxWidth + "px" : "auto";
					break;
				}
			}
		}
		;
	}
	;
	function fixMergedCellsSize(target) {
		var dc = $.data(target, "datagrid").dc;
		dc.body1.add(dc.body2).find("td.datagrid-td-merged").each(function() {
			var td = $(this);
			var colspan = td.attr("colspan") || 1;
			var width = getColumnOption(target, td.attr("field")).width;
			for ( var i = 1; i < colspan; i++) {
				td = td.next();
				width += getColumnOption(target, td.attr("field")).width + 1;
			}
			$(this).children("div.datagrid-cell")._outerWidth(width);
		});
	}
	;
	function fixEditorSize(target) {
		var dc = $.data(target, "datagrid").dc;
		dc.view.find("div.datagrid-editable").each(function() {
			var $this = $(this);
			var field = $this.parent().attr("field");
			var col = $(target).datagrid("getColumnOption", field);
			$this._outerWidth(col.width);
			var ed = $.data(this, "datagrid.editor");
			if (ed.actions.resize) {
				ed.actions.resize(ed.target, $this.width());
			}
		});
	}
	;
	function getColumnOption(target, field) {
		function getColumn(columns) {
			if (columns) {
				for ( var i = 0; i < columns.length; i++) {
					var cc = columns[i];
					for ( var j = 0; j < cc.length; j++) {
						var c = cc[j];
						if (c.field == field) {
							return c;
						}
					}
				}
			}
			return null;
		}
		;
		var opts = $.data(target, "datagrid").options;
		var col = getColumn(opts.columns);
		if (!col) {
			col = getColumn(opts.frozenColumns);
		}
		return col;
	}
	;
	function getColumnFields(target, frozen) {
		var opts = $.data(target, "datagrid").options;
		var columns = (frozen == true) ? (opts.frozenColumns || [ [] ]) : opts.columns;
		if (columns.length == 0) {
			return [];
		}
		var fields = [];
		function getFixedColspan(index) {
			var c = 0;
			var i = 0;
			while (true) {
				if (fields[i] == undefined) {
					if (c == index) {
						return i;
					}
					c++;
				}
				i++;
			}
		}
		;
		function findColumnFields(r) {
			var ff = [];
			var c = 0;
			for ( var i = 0; i < columns[r].length; i++) {
				var col = columns[r][i];
				if (col.field) {
					ff.push([ c, col.field ]);
				}
				c += parseInt(col.colspan || "1");
			}
			for ( var i = 0; i < ff.length; i++) {
				ff[i][0] = getFixedColspan(ff[i][0]);
			}
			for ( var i = 0; i < ff.length; i++) {
				var f = ff[i];
				fields[f[0]] = f[1];
			}
		}
		;
		for ( var i = 0; i < columns.length; i++) {
			findColumnFields(i);
		}
		return fields;
	}
	;
	function renderGrid(target, data) {
		var dg = $.data(target, "datagrid");
		var opts = dg.options;
		var dc = dg.dc;
		var selectedRows = dg.selectedRows;
		data = opts.loadFilter.call(target, data);
		dg.data = data;
		if (data.footer) {
			dg.footer = data.footer;
		}
		if (!opts.remoteSort) {
			var opt = getColumnOption(target, opts.sortName);
			if (opt) {
				var sorter = opt.sorter || function(a, b) {
					return (a > b ? 1 : -1);
				};
				data.rows.sort(function(r1, r2) {
					return sorter(r1[opts.sortName], r2[opts.sortName]) * (opts.sortOrder == "asc" ? 1 : -1);
				});
			}
		}
		if (opts.view.onBeforeRender) {
			opts.view.onBeforeRender.call(opts.view, target, data.rows);
		}
		opts.view.render.call(opts.view, target, dc.body2, false);
		opts.view.render.call(opts.view, target, dc.body1, true);
		if (opts.showFooter) {
			opts.view.renderFooter.call(opts.view, target, dc.footer2, false);
			opts.view.renderFooter.call(opts.view, target, dc.footer1, true);
		}
		if (opts.view.onAfterRender) {
			opts.view.onAfterRender.call(opts.view, target);
		}
		//add by zhengshj 2012-08-01 
		// 自动选中第一行
		if (data.rows.length && opts.singleSelect) {
			selectRow(target, 0);
		}
		opts.onLoadSuccess.call(target, data);
		var pager = $(target).datagrid("getPager");
		if (pager.length) {
			if (pager.pagination("options").total != data.total) {
				pager.pagination({
					total : data.total
				});
			}
		}
		fixRowHeight(target);
		dc.body2.triggerHandler("scroll");
		selectRows();
		$(target).datagrid("autoSizeColumn");
		function selectRows() {
			if (opts.idField) {
				for ( var i = 0; i < data.rows.length; i++) {
					var row = data.rows[i];
					if (isSelected(row)) {
						selectRecord(target, row[opts.idField]);
					}
				}
			}
			function isSelected(row) {
				for ( var i = 0; i < selectedRows.length; i++) {
					if (selectedRows[i][opts.idField] == row[opts.idField]) {
						selectedRows[i] = row;
						return true;
					}
				}
				return false;
			}
			;
		}
		;
	}
	;
	function getRowIndex(target, row) {
		var opts = $.data(target, "datagrid").options;
		var rows = $.data(target, "datagrid").data.rows;
		if (typeof row == "object") {
			return indexOf(rows, row);
		} else {
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i][opts.idField] == row) {
					return i;
				}
			}
			return -1;
		}
	}
	;
	function getSelected(target) {
		var opts = $.data(target, "datagrid").options;
		var data = $.data(target, "datagrid").data;
		if (opts.idField) {
			return $.data(target, "datagrid").selectedRows;
		} else {
			var rows = [];
			opts.finder.getTr(target, "", "selected", 2).each(function() {
				var rowIndex = parseInt($(this).attr("datagrid-row-index"));
				rows.push(data.rows[rowIndex]);
			});
			return rows;
		}
	}
	; 
	function selectRecord(target, id) {
		var opts = $.data(target, "datagrid").options;
		if (opts.idField) {
			var index = getRowIndex(target, id);
			if (index >= 0) {
				selectRow(target, index);
			}
		}
	}
	;
	function selectRow(target, rowIndex, checkOnSelect) {
		var dg = $.data(target, "datagrid");
		var dc = dg.dc;
		var opts = dg.options;
		var data = dg.data;
		var selectedRows = dg.selectedRows;
		if (opts.singleSelect) {
			unselectAll(target);
			selectedRows.splice(0, selectedRows.length);
		}
		if (!checkOnSelect && opts.checkOnSelect) {
			checkRow(target, rowIndex, true);
		}
		if (opts.idField) {
			var row = opts.finder.getRow(target, rowIndex);
			(function() {
				for ( var i = 0; i < selectedRows.length; i++) {
					if (selectedRows[i][opts.idField] == row[opts.idField]) {
						return;
					}
				}
				selectedRows.push(row);
			})();
		}
		opts.onSelect.call(target, rowIndex, data.rows[rowIndex]);
		//add by zhengshj 2012-08-01
		setRowState(target, rowIndex, "select");
		opts.selectIndex = rowIndex;
		var tr = opts.finder.getTr(target, rowIndex).addClass("datagrid-row-selected");
		if (tr.length) {
			var height = dc.view2.children("div.datagrid-header").outerHeight();
			var gridBody = dc.body2;
			var top = tr.position().top - height;
			if (top <= 0) {
				gridBody.scrollTop(gridBody.scrollTop() + top);
			} else {
				if (top + tr.outerHeight() > gridBody.height() - 18) {
					gridBody.scrollTop(gridBody.scrollTop() + top + tr.outerHeight() - gridBody.height() + 18);
				}
			}
		}
	}
	;
	function unselectRow(target, rowIndex, checkOnSelect) {
		var dg = $.data(target, "datagrid");
		var dc = dg.dc;
		var opts = dg.options;
		var data = dg.data;
		var selectedRows = dg.selectedRows;
		
		if (!checkOnSelect && opts.checkOnSelect) {
			uncheckRow(target, rowIndex, true);
		}
		opts.finder.getTr(target, rowIndex).removeClass("datagrid-row-selected");
		var row = opts.finder.getRow(target, rowIndex);
		if (opts.idField) {
			removeById(selectedRows, opts.idField, row[opts.idField]);
		}
		opts.onUnselect.call(target, rowIndex, row);
	}
	;
	function selectAll(target, checkOnSelect) {
		var dg = $.data(target, "datagrid");
		var opts = dg.options;
		var rows = dg.data.rows;
		var selectedRows = dg.selectedRows;
		if (!checkOnSelect && opts.checkOnSelect) {
			checkAll(target, true);
		}
		opts.finder.getTr(target, "", "allbody").addClass("datagrid-row-selected");
		if (opts.idField) {
			for ( var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
		
				(function() {
					var row = rows[rowIndex];
					for ( var i = 0; i < selectedRows.length; i++) {
						if (selectedRows[i][opts.idField] == row[opts.idField]) {
							return;
						}
					}
					selectedRows.push(row);
				})();
			}
		}
		opts.onSelectAll.call(target, rows);
	}
	;
	function unselectAll(target, checkOnSelect) {
		var dg = $.data(target, "datagrid");
		var opts = dg.options;
		var rows = dg.data.rows;
		var selectedRows = dg.selectedRows;
		if (!checkOnSelect && opts.checkOnSelect) {
			uncheckAll(target, true);
		}
		opts.finder.getTr(target, "", "selected").removeClass("datagrid-row-selected");
		if (opts.idField) {
			for ( var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
				removeById(selectedRows, opts.idField, rows[rowIndex][opts.idField]);
			}
		}
		opts.onUnselectAll.call(target, rows);
	}
	;
	function checkRow(target, rowIndex, selectOnCheck) {
		var dg = $.data(target, "datagrid");
		var opts = dg.options;
		var data = dg.data;
		if (!selectOnCheck && opts.selectOnCheck) {
			selectRow(target, rowIndex, true);
		}
		var ck = opts.finder.getTr(target, rowIndex).find("div.datagrid-cell-check input[type=checkbox]");
		$.fn.prop ? ck.prop("checked", true) : ck.attr("checked", true);
		opts.onCheck.call(target, rowIndex, data.rows[rowIndex]);
	}
	;	
	function uncheckRow(target, rowIndex, selectOnCheck) {
		var dg = $.data(target, "datagrid");
		var opts = dg.options;
		var data = dg.data;
		if (!selectOnCheck && opts.selectOnCheck) {
			unselectRow(target, rowIndex, true);
		}
		var ck = opts.finder.getTr(target, rowIndex).find("div.datagrid-cell-check input[type=checkbox]");
		$.fn.prop ? ck.prop("checked", false) : ck.attr("checked", false);
		opts.onUncheck.call(target, rowIndex, data.rows[rowIndex]);
	}
	;
	function checkAll(target, selectOnCheck) {
		var dg = $.data(target, "datagrid");
		var opts = dg.options;
		var data = dg.data;
		if (!selectOnCheck && opts.selectOnCheck) {
			selectAll(target, true);
		}
		var field = opts.finder.getTr(target, "", "allbody").find("div.datagrid-cell-check input[type=checkbox]");
		$.fn.prop ? field.prop("checked", true) : field.attr("checked", true);
		opts.onCheckAll.call(target, data.rows);
	}
	;
	function uncheckAll(target, selectOnCheck) {
		var dg = $.data(target, "datagrid");
		var opts = dg.options;
		var data = dg.data;
		if (!selectOnCheck && opts.selectOnCheck) {
			unselectAll(target, true);
		}
		var field = opts.finder.getTr(target, "", "allbody").find("div.datagrid-cell-check input[type=checkbox]");
		$.fn.prop ? field.prop("checked", false) : field.attr("checked", false);
		opts.onUncheckAll.call(target, data.rows);
	}
	;
	function beginEdit(target, rowIndex) {
		var opts = $.data(target, "datagrid").options;
		var tr = opts.finder.getTr(target, rowIndex);
		var row = opts.finder.getRow(target, rowIndex);
		if (tr.hasClass("datagrid-row-editing")) {
			return;
		}
		if (opts.onBeforeEdit.call(target, rowIndex, row) == false) {
			return;
		}
		tr.addClass("datagrid-row-editing");
		createEditor(target, rowIndex);
		fixEditorSize(target);
		tr.find("div.datagrid-editable").each(function() {
			var field = $(this).parent().attr("field");
			var ed = $.data(this, "datagrid.editor");
			ed.actions.setValue(ed.target, row[field]);
		});
		//modify by zhengshj 2012-08-06
		//validateRow(target, rowIndex);
	}
	;
	function stopEdit(target, rowIndex, revert) {
		var opts = $.data(target, "datagrid").options;
		var updatedRows = $.data(target, "datagrid").updatedRows;
		var insertedRows = $.data(target, "datagrid").insertedRows;
		var tr = opts.finder.getTr(target, rowIndex);
		var row = opts.finder.getRow(target, rowIndex);
		if (!tr.hasClass("datagrid-row-editing")) {
			return;
		}
		if (!revert) {
			if (!validateRow(target, rowIndex)) {
				return;
			}
			var changed = false;
			var newValues = {};
			tr.find("div.datagrid-editable").each(function() {
				var field = $(this).parent().attr("field");
				var ed = $.data(this, "datagrid.editor");
				var value = ed.actions.getValue(ed.target);
				if (row[field] != value && !(value == "" && row[field] == null)) {
					row[field] = value;
					changed = true;
					newValues[field] = value;
				}
			});
			if (changed) {
				if (indexOf(insertedRows, row) == -1) {
					if (indexOf(updatedRows, row) == -1) {
						setRowState(target, rowIndex, "updated");
						updatedRows.push(row);
					}
				}
			}
		}
		tr.removeClass("datagrid-row-editing");
		destroyEditor(target, rowIndex);
		$(target).datagrid("refreshRow", rowIndex);
		if (!revert) {
			opts.onAfterEdit.call(target, rowIndex, row, newValues);
		} else {
			opts.onCancelEdit.call(target, rowIndex, row);
		}

	}
	;	
	function getEditors(target, rowIndex) {
		var opts = $.data(target, "datagrid").options;
		var tr = opts.finder.getTr(target, rowIndex);
		var editors = [];
		tr.children("td").each(function() {
			var cell = $(this).find("div.datagrid-editable");
			if (cell.length) {
				var ed = $.data(cell[0], "datagrid.editor");
				editors.push(ed);
			}
		});
		return editors;
	}
	;
	function getEditor(target, options) {
		var editors = getEditors(target, options.index);
		for ( var i = 0; i < editors.length; i++) {
			if (editors[i].field == options.field) {
				return editors[i];
			}
		}
		return null;
	}
	;
	function createEditor(target, rowIndex) {
		var opts = $.data(target, "datagrid").options;
		var tr = opts.finder.getTr(target, rowIndex);
		tr.children("td").each(function() {
			var cell = $(this).find("div.datagrid-cell");
			var field = $(this).attr("field");
			var col = getColumnOption(target, field);
			if (col && col.editor) {
				var type, editorOpts;
				if (typeof col.editor == "string") {
					type = col.editor;
				} else {
					type = col.editor.type;
					editorOpts = col.editor.options;
				}
				var editor = opts.editors[type];
				
				if (editor && (!editorOpts || !editorOpts.readonly)) {
					var html = cell.html();
					var width = cell.outerWidth();
					cell.addClass("datagrid-editable");
					cell._outerWidth(width);
					cell.html("<table border=\"0\" cellspacing=\"0\" cellpadding=\"1\"><tr><td></td></tr></table>");
					cell.children("table").attr("align", col.align);
					cell.children("table").bind("click dblclick contextmenu", function(e) {
						e.stopPropagation();
					});
					$.data(cell[0], "datagrid.editor", {
						actions : editor,
						target : editor.init(cell.find("td"), editorOpts),
						field : field,
						type : type,
						oldHtml : html
					});
				}
			}
		});
		fixRowHeight(target, rowIndex, true);
	}
	;
	function destroyEditor(target, rowIndex) {
		var opts = $.data(target, "datagrid").options;
		var tr = opts.finder.getTr(target, rowIndex);
		tr.children("td").each(function() {
			var cell = $(this).find("div.datagrid-editable");
			if (cell.length) {
				var ed = $.data(cell[0], "datagrid.editor");
				if (ed.actions.destroy) {
					ed.actions.destroy(ed.target);
				}
				cell.html(ed.oldHtml);
				$.removeData(cell[0], "datagrid.editor");
				cell.removeClass("datagrid-editable");
				cell.css("width", "");
			}
		});
	}
	;
	function validateRow(target, rowIndex) {
		var tr = $.data(target, "datagrid").options.finder.getTr(target, rowIndex);
		if (!tr.hasClass("datagrid-row-editing")) {
			return true;
		}
		var vbox = tr.find(".validatebox-text");
		vbox.validatebox("validate");
		vbox.trigger("mouseleave");
		var invalid = tr.find(".validatebox-invalid");
		return invalid.length == 0;
	}
	;
	function getChanges(target, type) {
		var insertedRows = $.data(target, "datagrid").insertedRows;
		var deletedRows = $.data(target, "datagrid").deletedRows;
		var updatedRows = $.data(target, "datagrid").updatedRows;
		if (!type) {
			var rows = [];
			rows = rows.concat(insertedRows);
			rows = rows.concat(deletedRows);
			rows = rows.concat(updatedRows);
			return rows;
		} else {
			if (type == "inserted") {
				return insertedRows;
			} else {
				if (type == "deleted") {
					return deletedRows;
				} else {
					if (type == "updated") {
						return updatedRows;
					}
				}
			}
		}
		return [];
	}
	;
	function deleteRow(target, rowIndex) {
		var opts = $.data(target, "datagrid").options;
		var data = $.data(target, "datagrid").data;
		var insertedRows = $.data(target, "datagrid").insertedRows;
		var deletedRows = $.data(target, "datagrid").deletedRows;
		var selectedRows = $.data(target, "datagrid").selectedRows;
		$(target).datagrid("cancelEdit", rowIndex);
		var row = data.rows[rowIndex];
		if (indexOf(insertedRows, row) >= 0) {
			removeById(insertedRows, row);
		} else {
			deletedRows.push(row);
		}
		removeById(selectedRows, opts.idField, data.rows[rowIndex][opts.idField]);
		opts.view.deleteRow.call(opts.view, target, rowIndex);
		if (opts.height == "auto") {
			fixRowHeight(target);
		}
	}
	;
	function insertRow(target, param) {
		var view = $.data(target, "datagrid").options.view;
		var insertedRows = $.data(target, "datagrid").insertedRows;
		view.insertRow.call(view, target, param.index, param.row);
		insertedRows.push(param.row);
		//add by zhengshj 2012-08-01
		setRowState(target, param.index, "inserted");
	}
	;
	function appendRow(target, row) {
		var view = $.data(target, "datagrid").options.view;
		var insertedRows = $.data(target, "datagrid").insertedRows;
		view.insertRow.call(view, target, null, row);
		insertedRows.push(row);
		//add by zhengshj 2012-08-01
		var editIndex = $(target).datagrid("getRows").length - 1;
		setRowState(target, editIndex, "inserted");
	}
	;
	function resetOperation(target) {
		var data = $.data(target, "datagrid").data;
		var rows = data.rows;
		var originalRows = [];
		for ( var i = 0; i < rows.length; i++) {
			originalRows.push($.extend({}, rows[i]));
		}
		$.data(target, "datagrid").originalRows = originalRows;
		$.data(target, "datagrid").updatedRows = [];
		$.data(target, "datagrid").insertedRows = [];
		$.data(target, "datagrid").deletedRows = [];
	}
	;
	function acceptChanges(target) {
		var data = $.data(target, "datagrid").data;
		var ok = true;
		for ( var i = 0, len = data.rows.length; i < len; i++) {
			if (validateRow(target, i)) {
				stopEdit(target, i, false);
			} else {
				ok = false;
			}
		}
		if (ok) {
			resetOperation(target);
		}
	}
	;
	function rejectChanges(target) {
		var opts = $.data(target, "datagrid").options;
		var originalRows = $.data(target, "datagrid").originalRows;
		var insertedRows = $.data(target, "datagrid").insertedRows;
		var deletedRows = $.data(target, "datagrid").deletedRows;
		var selectedRows = $.data(target, "datagrid").selectedRows;
		var data = $.data(target, "datagrid").data;
		for ( var i = 0; i < data.rows.length; i++) {
			stopEdit(target, i, true);
		}
		var records = [];
		for ( var i = 0; i < selectedRows.length; i++) {
			records.push(selectedRows[i][opts.idField]);
		}
		selectedRows.splice(0, selectedRows.length);
		data.total += deletedRows.length - insertedRows.length;
		data.rows = originalRows;
		renderGrid(target, data);
		for ( var i = 0; i < records.length; i++) {
			selectRecord(target, records[i]);
		}
		resetOperation(target);
	}
	;
	function request(target, param) {
		var opts = $.data(target, "datagrid").options;
		if (param) {
			opts.queryParams = param;
		}
		var param = $.extend({}, opts.queryParams);
		if (opts.pagination) {
			$.extend(param, {
				page : opts.pageNumber,
				rows : opts.pageSize
			});
		}
		if (opts.sortName) {
			$.extend(param, {
				sort : opts.sortName,
				order : opts.sortOrder
			});
		}
		if (opts.onBeforeLoad.call(target, param) == false) {
			return;
		}
		$(target).datagrid("loading");
		setTimeout(function() {
			doRequest();
		}, 0);
		function doRequest() {
			var loader = opts.loader.call(target, param, function(data) {
					setTimeout(function() {
						$(target).datagrid("loaded");
					}, 0);
					renderGrid(target, data);
					setTimeout(function() {
						resetOperation(target);
					}, 0);
			}, function() {
					setTimeout(function() {
						$(target).datagrid("loaded");
					}, 0);
				opts.onLoadError.apply(target, arguments);
			});
			if (loader == false) {
				$(target).datagrid("loaded");
			}
		}
		;
	}
	;
	function mergeCells(target, options) {
		var opts = $.data(target, "datagrid").options;
		var rows = $.data(target, "datagrid").data.rows;
		options.rowspan = options.rowspan || 1;
		options.colspan = options.colspan || 1;
		if (options.index < 0 || options.index >= rows.length) {
			return;
		}
		if (options.rowspan == 1 && options.colspan == 1) {
			return;
		}
		var field = rows[options.index][options.field];
		var tr = opts.finder.getTr(target, options.index);
		var td = tr.find("td[field=\"" + options.field + "\"]");
		td.attr("rowspan", options.rowspan).attr("colspan", options.colspan);
		td.addClass("datagrid-td-merged");
		for ( var i = 1; i < options.colspan; i++) {
			td = td.next();
			td.hide();
			rows[options.index][td.attr("field")] = field;
		}
		for ( var i = 1; i < options.rowspan; i++) {
			tr = tr.next();
			var td = tr.find("td[field=\"" + options.field + "\"]").hide();
			rows[options.index + i][td.attr("field")] = field;
			for ( var j = 1; j < options.colspan; j++) {
				td = td.next();
				td.hide();
				rows[options.index + i][td.attr("field")] = field;
			}
		}		
		fixMergedCellsSize(target);
	}
	;
	// 设置行状态
	function setRowState(target, rowIndex, state) {
		var opts = $.data(target, "datagrid").options;
		if (opts.editing) {
			var tr = opts.finder.getTr(target, rowIndex, 'body', '1');
			var div = $("td[field=\"_state\"] > .datagrid-cell-rownumber",tr);
			if(div.length){
				if(div.hasClass("datagrid-cell-state-add") || div.hasClass("datagrid-cell-state-edit")){
					return;
				}
				if(div.hasClass("datagrid-cell-state-select")){
					div.removeClass("datagrid-cell-state-select");
				}
				if(state == "select"){
					//去除上一选中行状态
					var trPrev = opts.finder.getTr(target, opts.selectIndex, 'body', '1');
					var divPrev = $("td[field=\"_state\"] > .datagrid-cell-rownumber",trPrev);
					if(divPrev.length){
						divPrev.removeClass("datagrid-cell-state-select");
					}
					div.addClass("datagrid-cell-state-select");
				} else if(state == "inserted"){
					div.addClass("datagrid-cell-state-add");
				} else if(state == "updated"){
					div.addClass("datagrid-cell-state-edit");
				}
			}
		}
	}
	;
	$.fn.datagrid = function(options, param) {
		if (typeof options == "string") {
			return $.fn.datagrid.methods[options](this, param);
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "datagrid");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.options = opts;
			} else {
				opts = $.extend({}, $.extend({}, $.fn.datagrid.defaults, {
					queryParams : {}
				}), $.fn.datagrid.parseOptions(this), options);
				$(this).css("width", "").css("height", "");
				var gridWrap = wrapGrid(this, opts.rownumbers);
				if (!opts.columns) {
					opts.columns = gridWrap.columns;
				}
				if (!opts.frozenColumns) {
					opts.frozenColumns = gridWrap.frozenColumns;
				}
				$.data(this, "datagrid", {
					options : opts,
					panel : gridWrap.panel,
					dc : gridWrap.dc,
					selectedRows : [],
					data : {
						total : 0,
						rows : []
					},
					originalRows : [],
					updatedRows : [],
					insertedRows : [],
					deletedRows : []
				});
			}
			init(this);
			if (!state) {
				var data = getGridData(this);
				if (data.total > 0) {
					renderGrid(this, data);
					resetOperation(this);
				}
			}
			setSize(this);	
			request(this);
			setProperties(this);
		});
	};
	var editors = {
		text : {
			init : function(container, options) {
				var editor = $("<input type=\"text\" class=\"datagrid-editable-input\">").appendTo(container);
				return editor;
			},
			getValue : function(target) {
				return $(target).val();
			},
			setValue : function(target, value) {
				$(target).val(value);
			},
			resize : function(target, width) {
				$(target)._outerWidth(width);
			}
		},
		textarea : {
			init : function(container, options) {
				var editor = $("<textarea class=\"datagrid-editable-input\"></textarea>").appendTo(container);
				return editor;
			},
			getValue : function(target) {
				return $(target).val();
			},
			setValue : function(target, value) {
				$(target).val(value);
			},
			resize : function(target, width) {
				$(target)._outerWidth(width);
			}
		},
		checkbox : {
			init : function(container, options) {
				var editor = $("<input type=\"checkbox\">").appendTo(container);
				editor.val(options.on);
				editor.attr("offval", options.off);
				return editor;
			},
			getValue : function(target) {
				if ($(target).is(":checked")) {
					return $(target).val();
				} else {
					return $(target).attr("offval");
				}
			},
			setValue : function(target, value) {
				var checked = false;
				if ($(target).val() == value) {
					checked = true;
				}
				$.fn.prop ? $(target).prop("checked", checked) : $(target).attr("checked", checked);
			}
		},
		numberbox : {
			init : function(container, options) {
				var editor = $("<input type=\"text\" class=\"datagrid-editable-input\">").appendTo(container);
				editor.numberbox(options);
				return editor;
			},
			destroy : function(target) {
				$(target).numberbox("destroy");
			},
			getValue : function(target) {
				return $(target).numberbox("getValue");
			},
			setValue : function(target, value) {
				$(target).numberbox("setValue", value);
			},
			resize : function(target, width) {
				$(target)._outerWidth(width);
			}
		},
		validatebox : {
			init : function(container, options) {
				var editor = $("<input type=\"text\" class=\"datagrid-editable-input\">").appendTo(container);
				editor.validatebox(options);
				return editor;
			},
			destroy : function(target) {
				$(target).validatebox("destroy");
			},
			getValue : function(target) {
				return $(target).val();
			},
			setValue : function(target, value) {
				$(target).val(value);
			},
			resize : function(target, width) {
				$(target)._outerWidth(width);
			}
		},
		datebox : {
			init : function(container, options) {
				var editor = $("<input type=\"text\">").appendTo(container);
				editor.datebox(options);
				return editor;
			},
			destroy : function(target) {
				$(target).datebox("destroy");
			},
			getValue : function(target) {
				return $(target).datebox("getValue");
			},
			setValue : function(target, value) {
				$(target).datebox("setValue", value);
			},
			resize : function(target, width) {
				$(target).datebox("resize", width);
			}
		},
		combobox : {
			init : function(container, options) {
				var editor = $("<input type=\"text\">").appendTo(container);
				editor.combobox(options || {});
				return editor;
			},
			destroy : function(target) {
				$(target).combobox("destroy");
			},
			getValue : function(target) {
				return $(target).combobox("getValue");
			},
			setValue : function(target, value) {
				$(target).combobox("setValue", value);
			},
			resize : function(target, width) {
				$(target).combobox("resize", width);
			}
		},
		combotree : {
			init : function(container, options) {
				var editor = $("<input type=\"text\">").appendTo(container);
				editor.combotree(options);
				return editor;
			},
			destroy : function(target) {
				$(target).combotree("destroy");
			},
			getValue : function(target) {
				return $(target).combotree("getValue");
			},
			setValue : function(target, value) {
				$(target).combotree("setValue", value);
			},
			resize : function(target, width) {
				$(target).combotree("resize", width);
			}
		}
	};
	$.fn.datagrid.methods = {
		options : function(jq) {
			var gridOpts = $.data(jq[0], "datagrid").options;
			var panelOpts = $.data(jq[0], "datagrid").panel.panel("options");
			var opts = $.extend(gridOpts, {
				width : panelOpts.width,
				height : panelOpts.height,
				closed : panelOpts.closed,
				collapsed : panelOpts.collapsed,
				minimized : panelOpts.minimized,
				maximized : panelOpts.maximized
			});
			var pager = jq.datagrid("getPager");
			if (pager.length) {
				var pagerOpts = pager.pagination("options");
				$.extend(opts, {
					pageNumber : pagerOpts.pageNumber,
					pageSize : pagerOpts.pageSize
				});
			}
			return opts;
		},
		getPanel : function(jq) {
			return $.data(jq[0], "datagrid").panel;
		},
		getPager : function(jq) {
			return $.data(jq[0], "datagrid").panel.find("div.datagrid-pager");
		},
		getColumnFields : function(jq, frozen) {
			return getColumnFields(jq[0], frozen);
		},
		getColumnOption : function(jq, field) {
			return getColumnOption(jq[0], field);
		},
		resize : function(jq, param) {
			return jq.each(function() {
				setSize(this, param);
			});
		},
		load : function(jq, param) {
			return jq.each(function() {
				var opts = $(this).datagrid("options");
				opts.pageNumber = 1;
				var pager = $(this).datagrid("getPager");
				pager.pagination({
					pageNumber : 1
				});
				request(this, param);
			});
		},
		reload : function(jq, param) {
			return jq.each(function() {
				request(this, param);
			});
		},
		reloadFooter : function(jq, footer) {
			return jq.each(function() {
				var opts = $.data(this, "datagrid").options;
				var dc = $.data(this, "datagrid").dc;
				if (footer) {
					$.data(this, "datagrid").footer = footer;
				}
				if (opts.showFooter) {
					opts.view.renderFooter.call(opts.view, this, dc.footer2, false);
					opts.view.renderFooter.call(opts.view, this, dc.footer1, true);
					if (opts.view.onAfterRender) {
						opts.view.onAfterRender.call(opts.view, this);
					}
					$(this).datagrid("fixRowHeight");
				}
			});
		},
		loading : function(jq) {
			return jq.each(function() {
				var opts = $.data(this, "datagrid").options;
				$(this).datagrid("getPager").pagination("loading");
				if (opts.loadMsg) {
					var wrap = $(this).datagrid("getPanel");
					$("<div class=\"datagrid-mask\" style=\"display:block\"></div>").appendTo(wrap);
					var msg = $("<div class=\"datagrid-mask-msg\" style=\"display:block\"></div>").html(opts.loadMsg).appendTo(wrap);
					msg.css("left", (wrap.width() - msg.outerWidth()) / 2);
				}
			});
		},
		loaded : function(jq) {
			return jq.each(function() {
				$(this).datagrid("getPager").pagination("loaded");
				var wrap = $(this).datagrid("getPanel");
				wrap.children("div.datagrid-mask-msg").remove();
				wrap.children("div.datagrid-mask").remove();
			});
		},
		fitColumns : function(jq) {
			return jq.each(function() {
				fitColumns(this);
			});
		},
		fixColumnSize : function(jq) {
			return jq.each(function() {
				fixColumnSize(this);
			});
		},
		fixRowHeight : function(jq, index) {
			return jq.each(function() {
				fixRowHeight(this, index);
			});
		},
		autoSizeColumn : function(jq, field) {
			return jq.each(function() {
				autoSizeColumn(this, field);
			});
		},
		loadData : function(jq, data) {
			return jq.each(function() {
				renderGrid(this, data);
				resetOperation(this);
			});
		},
		getData : function(jq) {
			return $.data(jq[0], "datagrid").data;
		},
		getRows : function(jq) {
			return $.data(jq[0], "datagrid").data.rows;
		},
		getFooterRows : function(jq) {
			return $.data(jq[0], "datagrid").footer;
		},
		getRowIndex : function(jq, id) {
			return getRowIndex(jq[0], id);
		},
		getChecked : function(jq) {
			var rr = [];
			var rows = jq.datagrid("getRows");
			var dc = $.data(jq[0], "datagrid").dc;
			dc.view.find("div.datagrid-cell-check input:checked").each(function() {
				var _19e = $(this).parents("tr.datagrid-row:first").attr("datagrid-row-index");
				rr.push(rows[_19e]);
			});
			return rr;
		},
		getSelected : function(jq) {
			var rows = getSelected(jq[0]);
			return rows.length > 0 ? rows[0] : null;
		},
		getSelections : function(jq) {
			return getSelected(jq[0]);
		},
		clearSelections : function(jq) {
			return jq.each(function() {
				var selectedRows = $.data(this, "datagrid").selectedRows;
				selectedRows.splice(0, selectedRows.length);
				unselectAll(this);
			});
		},
		selectAll : function(jq) {
			return jq.each(function() {
				selectAll(this);
			});
		},
		unselectAll : function(jq) {
			return jq.each(function() {
				unselectAll(this);
			});
		},
		selectRow : function(jq, index) {
			return jq.each(function() {
				selectRow(this, index);
			});
		},
		selectRecord : function(jq, id) {
			return jq.each(function() {
				selectRecord(this, id);
			});
		},
		unselectRow : function(jq, index) {
			return jq.each(function() {
				unselectRow(this, index);
			});
		},
		checkRow : function(jq, index) {
			return jq.each(function() {
				checkRow(this, index);
			});
		},
		uncheckRow : function(jq, index) {
			return jq.each(function() {
				uncheckRow(this, index);
			});
		},
		checkAll : function(jq) {
			return jq.each(function() {
				checkAll(this);
			});
		},
		uncheckAll : function(jq) {
			return jq.each(function() {
				uncheckAll(this);
			});
		},
		beginEdit : function(jq, index) {
			return jq.each(function() {
				beginEdit(this, index);
			});
		},
		endEdit : function(jq, index) {
			return jq.each(function() {
				stopEdit(this, index, false);
			});
		},
		cancelEdit : function(jq, index) {
			return jq.each(function() {
				stopEdit(this, index, true);
			});
		},
		getEditors : function(jq, index) {
			return getEditors(jq[0], index);
		},
		getEditor : function(jq, options) {
			return getEditor(jq[0], options);
		},
		refreshRow : function(jq, index) {
			return jq.each(function() {
				var opts = $.data(this, "datagrid").options;
				opts.view.refreshRow.call(opts.view, this, index);
			});
		},
		validateRow : function(jq, index) {
			return validateRow(jq[0], index);
		},
		updateRow : function(jq, index) {
			return jq.each(function() {
				var opts = $.data(this, "datagrid").options;
				opts.view.updateRow.call(opts.view, this, index.index, index.row);
			});
		},
		appendRow : function(jq, row) {
			return jq.each(function() {
				appendRow(this, row);
			});
		},
		insertRow : function(jq, param) {
			return jq.each(function() {
				insertRow(this, param);
			});
		},
		deleteRow : function(jq, wrap) {
			return jq.each(function() {
				deleteRow(this, wrap);
			});
		},
		getChanges : function(jq, type) {
			return getChanges(jq[0], type);
		},
		acceptChanges : function(jq) {
			return jq.each(function() {
				acceptChanges(this);
			});
		},
		rejectChanges : function(jq) {
			return jq.each(function() {
				rejectChanges(this);
			});
		},
		mergeCells : function(jq, options) {
			return jq.each(function() {
				mergeCells(this, options);
			});
		},
		showColumn : function(jq, field) {
			return jq.each(function() {
				var panel = $(this).datagrid("getPanel");
				panel.find("td[field=\"" + field + "\"]").show();
				$(this).datagrid("getColumnOption", field).hidden = false;
				$(this).datagrid("fitColumns");
			});
		},
		hideColumn : function(jq, field) {
			return jq.each(function() {
				var panel = $(this).datagrid("getPanel");
				panel.find("td[field=\"" + field + "\"]").hide();
				$(this).datagrid("getColumnOption", field).hidden = true;
				$(this).datagrid("fitColumns");
			});
		}
	};
	$.fn.datagrid.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.panel.parseOptions(target), $.parser.parseOptions(target, [ "url", "command", "toolbar", "idField", "sortName", "sortOrder", "pagePosition", {
			fitColumns : "boolean",
			autoRowHeight : "boolean",
			striped : "boolean",
			nowrap : "boolean"
		}, {
			rownumbers : "boolean",
			singleSelect : "boolean",
			checkOnSelect : "boolean",
			selectOnCheck : "boolean"
		}, {
			pagination : "boolean",
			pageSize : "number",
			pageNumber : "number"
		}, {
			remoteSort : "boolean",
			showHeader : "boolean",
			showFooter : "boolean"
		}, {
			scrollbarSize : "number"
		} ]), {
			pageList : (t.attr("pageList") ? eval(t.attr("pageList")) : undefined),
			loadMsg : (t.attr("loadMsg") != undefined ? t.attr("loadMsg") : undefined),
			showIndicator : (t.attr("showIndicator") ? t.attr("showIndicator") == "true" : undefined),//add by zhengshj 2012-08-01
			editing : (t.attr("editing") ? t.attr("editing") == "true" : true),//add by zhengshj 2012-08-01
			rowStyler : (t.attr("rowStyler") ? eval(t.attr("rowStyler")) : undefined)
		});
	};
	var view = {
		//渲染表格主体
		render : function(target, container, frozen) {
			var dg = $.data(target, "datagrid");
			var opts = dg.options;
			var rows = dg.data.rows;
			var fields = $(target).datagrid("getColumnFields", frozen);
			if (frozen) {
				if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))) {
					return;
				}
			}
			var html = [ "<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" ];
			for ( var i = 0; i < rows.length; i++) {
				var cls = (i % 2 && opts.striped) ? "class=\"datagrid-row datagrid-row-alt\"" : "class=\"datagrid-row\"";
				var style = opts.rowStyler ? opts.rowStyler.call(target, i, rows[i]) : "";
				var style2 = style ? "style=\"" + style + "\"" : "";
				var id = dg.rowIdPrefix + "-" + (frozen ? 1 : 2) + "-" + i;
				html.push("<tr id=\"" + id + "\" datagrid-row-index=\"" + i + "\" " + cls + " " + style2 + ">");
				html.push(this.renderRow.call(this, target, fields, frozen, i, rows[i]));
				html.push("</tr>");
			}
			html.push("</tbody></table>");
			$(container).html(html.join(""));
		},
		//渲染表格尾部
		renderFooter : function(target, container, frozen) {
			var opts = $.data(target, "datagrid").options;
			var rows = $.data(target, "datagrid").footer || [];
			var fields = $(target).datagrid("getColumnFields", frozen);
			var html = [ "<table class=\"datagrid-ftable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" ];
			for ( var i = 0; i < rows.length; i++) {
				html.push("<tr class=\"datagrid-row\" datagrid-row-index=\"" + i + "\">");
				html.push(this.renderRow.call(this, target, fields, frozen, i, rows[i]));
				html.push("</tr>");
			}
			html.push("</tbody></table>");
			$(container).html(html.join(""));
		},
		//渲染表格行
		renderRow : function(target, fields, frozen, rowIndex, rowData) {
			var opts = $.data(target, "datagrid").options;
			var cc = [];
			if (frozen && opts.rownumbers) {
				var rowNumber = rowIndex + 1;
				if (opts.pagination) {
					rowNumber += (opts.pageNumber - 1) * opts.pageSize;
				}
				cc.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">" + rowNumber + "</div></td>");
				if (opts.showIndicator) {
					cc.push("<td field=\"_state\" class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">&nbsp;</div></td>");
				}
			}
			for ( var i = 0; i < fields.length; i++) {
				var field = fields[i];
				var col = $(target).datagrid("getColumnOption", field);
				if (col) {
					var style = col.styler ? (col.styler(rowData[field], rowData, rowIndex) || "") : "";
					var style2 = col.hidden ? "style=\"display:none;" + style + "\"" : (style ? "style=\"" + style + "\"" : "");
					cc.push("<td field=\"" + field + "\" " + style2 + ">");
					if (col.checkbox) {
						var style2 = "";
					} else {
						var style2 = "";
						style2 += "text-align:" + (col.align || "left") + ";";
						if (!opts.nowrap) {
							style2 += "white-space:normal;height:auto;";
						} else {
							if (opts.autoRowHeight) {
								style2 += "height:auto;";
							}
						}
					}
					cc.push("<div style=\"" + style2 + "\" ");
					if (col.checkbox) {
						cc.push("class=\"datagrid-cell-check ");
					} else {
						cc.push("class=\"datagrid-cell "+ col.cellClass);
					}
					cc.push("\">");
					if (col.checkbox) {
						cc.push("<input type=\"checkbox\" name=\"" + field + "\" value=\"" + (rowData[field] != undefined ? rowData[field] : "") + "\"/>");
					} else {
						//add by zhengshj 2012-08-01
						var fieldData;
						try {
							fieldData = eval("rowData['" + field.replace(/\./g, "']['") + "']");
						} catch (err) {
							fieldData = ""; // 找不到对象，则赋值为空串
						}
						if (col.dropdown) {
							cc.push($(col.dropdown).dropdown("getText", fieldData));
						} else if (col.formatter) {
							cc.push(col.formatter(fieldData, rowData, rowIndex));
						} else {
							cc.push(fieldData);
						}
					}
					cc.push("</div>");
					cc.push("</td>");
				}
			}
			return cc.join("");
		},
		refreshRow : function(target, rowIndex) {
			var row = {};
			var fields = $(target).datagrid("getColumnFields", true).concat($(target).datagrid("getColumnFields", false));
			for ( var i = 0; i < fields.length; i++) {
				row[fields[i]] = undefined;
			}
			var rows = $(target).datagrid("getRows");
			$.extend(row, rows[rowIndex]);
			this.updateRow.call(this, target, rowIndex, row);
		},
		updateRow : function(target, rowIndex, row) {
			var opts = $.data(target, "datagrid").options;
			var rows = $(target).datagrid("getRows");
			var tr = opts.finder.getTr(target, rowIndex);
			for ( var field in row) {
				rows[rowIndex][field] = row[field];
				var td = tr.children("td[field=\"" + field + "\"]");
				var cell = td.find("div.datagrid-cell");
				var col = $(target).datagrid("getColumnOption", field);
				if (col) {
					var style = col.styler ? col.styler(rows[rowIndex][field], rows[rowIndex], rowIndex) : "";
					td.attr("style", style || "");
					if (col.hidden) {
						td.hide();
					}
					//add by zhengshj 2012-08-01
					if (col.dropdown) {
						cell.html($(col.dropdown).dropdown("getText", rows[rowIndex][field]));
					} else if (col.formatter) {
						cell.html(col.formatter(rows[rowIndex][field], rows[rowIndex], rowIndex));
					} else {
						cell.html(rows[rowIndex][field]);
					}
				}
			}
			var style = opts.rowStyler ? opts.rowStyler.call(target, rowIndex, rows[rowIndex]) : "";
			tr.attr("style", style || "");
			$(target).datagrid("fixRowHeight", rowIndex);
		},
		insertRow : function(target, rowIndex, row) {
			var opts = $.data(target, "datagrid").options;
			var dc = $.data(target, "datagrid").dc;
			var data = $.data(target, "datagrid").data;
			if (rowIndex == undefined || rowIndex == null) {
				rowIndex = data.rows.length;
			}
			if (rowIndex > data.rows.length) {
				rowIndex = data.rows.length;
			}
			for ( var i = data.rows.length - 1; i >= rowIndex; i--) {
				opts.finder.getTr(target, i, "body", 2).attr("datagrid-row-index", i + 1);
				var tr = opts.finder.getTr(target, i, "body", 1).attr("datagrid-row-index", i + 1);
				if (opts.rownumbers) {
					tr.find("div.datagrid-cell-rownumber").html(i + 2);
				}
			}
			var frozenFields = $(target).datagrid("getColumnFields", true);
			var fields = $(target).datagrid("getColumnFields", false);
			var tr1 = "<tr class=\"datagrid-row\" datagrid-row-index=\"" + rowIndex + "\">" + this.renderRow.call(this, target, frozenFields, true, rowIndex, row) + "</tr>";
			var tr2 = "<tr class=\"datagrid-row\" datagrid-row-index=\"" + rowIndex + "\">" + this.renderRow.call(this, target, fields, false, rowIndex, row) + "</tr>";
			if (rowIndex >= data.rows.length) {
				if (data.rows.length) {
					opts.finder.getTr(target, "", "last", 1).after(tr1);
					opts.finder.getTr(target, "", "last", 2).after(tr2);
				} else {
					dc.body1.html("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" + tr1 + "</tbody></table>");
					dc.body2.html("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" + tr2 + "</tbody></table>");
				}
			} else {
				opts.finder.getTr(target, rowIndex + 1, "body", 1).before(tr1);
				opts.finder.getTr(target, rowIndex + 1, "body", 2).before(tr2);
			}
			data.total += 1;
			data.rows.splice(rowIndex, 0, row);
			this.refreshRow.call(this, target, rowIndex);
		},
		deleteRow : function(target, rowIndex) {
			var opts = $.data(target, "datagrid").options;
			var data = $.data(target, "datagrid").data;
			opts.finder.getTr(target, rowIndex).remove();
			for ( var i = rowIndex + 1; i < data.rows.length; i++) {
				opts.finder.getTr(target, i, "body", 2).attr("datagrid-row-index", i - 1);
				var tr1 = opts.finder.getTr(target, i, "body", 1).attr("datagrid-row-index", i - 1);
				if (opts.rownumbers) {
					tr1.find("div.datagrid-cell-rownumber").html(i);
				}
			}
			data.total -= 1;
			data.rows.splice(rowIndex, 1);
		},
		onBeforeRender : function(target, rows) {
		},
		onAfterRender : function(target) {
			var opts = $.data(target, "datagrid").options;
			if (opts.showFooter) {
				var footer = $(target).datagrid("getPanel").find("div.datagrid-footer");
				footer.find("div.datagrid-cell-rownumber,div.datagrid-cell-check").css("visibility", "hidden");
			}
		}
	};
	$.fn.datagrid.defaults = $.extend({}, $.fn.panel.defaults, {
		frozenColumns : undefined,
		columns : undefined,
		fitColumns : false,
		autoRowHeight : true,
		toolbar : null,
		striped : false,
		method : "post",
		nowrap : true,
		idField : null,
		url : null,
		loadMsg : "正在处理，请稍待。。。",
		rownumbers : false,
		singleSelect : false,
		selectOnCheck : true,
		checkOnSelect : true,
		pagination : false,
		pagePosition : "bottom",
		pageNumber : 1,
		pageSize : 10,
		pageList : [ 10, 20, 30, 40, 50 ],
		queryParams : {},
		sortName : null,
		sortOrder : "asc",
		remoteSort : true,
		showHeader : true,
		showFooter : false,
		scrollbarSize : 18,
		showIndicator : false,
		editing : true,
		editIndex : -1,
		selectIndex : -1,
		rowStyler : function(rowIndex, rowData) {
		},
		loader : function(param, success, error) {
			var opts = $(this).datagrid("options");
			if (!opts.url) {
				return false;
			}
			$.ajax({
				type : opts.method,
				url : opts.url,
				data : param,
				dataType : "json",
				success : function(data) {
					success(data);
				},
				error : function() {
					error.apply(this, arguments);
				}
			});
		},
		loadFilter : function(data) {
			if (typeof data.length == "number" && typeof data.splice == "function") {
				return {
					total : data.length,
					rows : data
				};
			} else {
				return data;
			}
		},
		editors : editors,
		finder : {
			getTr : function(target, rowIndex, type, tag) {
				type = type || "body";
				tag = tag || 0;
				var dg = $.data(target, "datagrid");
				var dc = dg.dc;
				var opts = dg.options;
				if (tag == 0) {
					var tr1 = opts.finder.getTr(target, rowIndex, type, 1);
					var tr2 = opts.finder.getTr(target, rowIndex, type, 2);
					return tr1.add(tr2);
				} else {
					if (type == "body") {
						var tr = $("#" + dg.rowIdPrefix + "-" + tag + "-" + rowIndex);
						if (!tr.length) {
							tr = (tag == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr[datagrid-row-index=" + rowIndex + "]");
						}
						return tr;
					} else {
						if (type == "footer") {
							return (tag == 1 ? dc.footer1 : dc.footer2).find(">table>tbody>tr[datagrid-row-index=" + rowIndex + "]");
						} else {
							if (type == "selected") {
								return (tag == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr.datagrid-row-selected");
							} else {
								if (type == "last") {
									return (tag == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr:last[datagrid-row-index]");
								} else {
									if (type == "allbody") {
										return (tag == 1 ? dc.body1 : dc.body2).find(">table>tbody>tr[datagrid-row-index]");
									} else {
										if (type == "allfooter") {
											return (tag == 1 ? dc.footer1 : dc.footer2).find(">table>tbody>tr[datagrid-row-index]");
										}
									}
								}
							}
						}
					}
				}
			},
			getRow : function(target, rowIndex) {
				return $.data(target, "datagrid").data.rows[rowIndex];
			}
		},
		view : view,
		onBeforeLoad : function(param) {
		},
		onLoadSuccess : function() {
		},
		onLoadError : function() {
		},
		onClickRow : function(rowIndex, rowData) {
		},
		onDblClickRow : function(rowIndex, rowData) {
		},
		onClickCell : function(rowIndex, rowData, value) {
		},
		onDblClickCell : function(rowIndex, rowData, value) {
		},
		onSortColumn : function(sort, order) {
		},
		onResizeColumn : function(sort, order) {
		},
		onSelect : function(rowIndex, rowData) {
		},
		onUnselect : function(rowIndex, rowData) {
		},
		onSelectAll : function(rows) {
		},
		onUnselectAll : function(rows) {
		},
		onCheck : function(rowIndex, rowData) {
		},
		onUncheck : function(rowIndex, rowData) {
		},
		onCheckAll : function(rows) {
		},
		onUncheckAll : function(rows) {
		},
		onBeforeEdit : function(rowIndex, rowData) {
		},
		onAfterEdit : function(rowIndex, rowData, changes) {
		},
		onCancelEdit : function(rowIndex, rowData) {
		},
		onHeaderContextMenu : function(e, field) {
		},
		onRowContextMenu : function(e, rowIndex, rowData) {
		}
	});
})(jQuery);

$.extend($.fn.datagrid.defaults.editors, { 
	dropdown : {
		init : function(container, options) {
			var ipts = $('<span class="searchbox"></span>').appendTo(container);
			var editable = options.editable ? true : false;
			var readonly = editable ? "" : "readonly";
			var input = $('<input type="text" class=\"datagrid-editable-input\" '+readonly+' />').appendTo(ipts);
			input.validatebox(options);
			var btn = $("<span><span class=\"searchbox-button\"></span></span>").appendTo(ipts);
			btn.bind("click", function() {
				options.callback(input);
			});
			return ipts;
		},
		getValue : function(target) {
			return $(target).find('.datagrid-editable-input').val();
		},
		display : function(target, value) {
			var btn = $(target).find('.searchbox-button');
			if (value == "none") {
				btn.css('display', 'none');
			} else {
				btn.css('display', '');
			}
		},
		setValue : function(target, value) {
			$(target).find('.datagrid-editable-input').val(value);
		},
		resize : function(target, width) {
			var input = $(target).find('.datagrid-editable-input');
			var btn = $(target).find('.searchbox-button');
			if ($.boxModel == true) {
				input.width(width - btn.outerWidth() - (input.outerWidth() - input.width()));
			} else {
				input.width(width - btn.width);
			}
		}
	}
});

$.extend($.fn.datagrid.methods,{
	enableEditing : function(jq) {
		return jq.each(function() {
			var opts = $.data(this, 'datagrid').options;
			opts.editing = true;
		});
	},
	disableEditing : function(jq) {
		return jq.each(function() {
			var opts = $.data(this, 'datagrid').options;
			opts.editing = false;
		});
	},
	addRow : function(jq, row) {
		return jq.each(function() {
			var $this = $(this);
			var opts = $.data(this, 'datagrid').options;
			// 结束上次编辑
			if (opts.editIndex >= 0) {
				// 校验不通过，则返回
				if (!$this.datagrid('validateRow', opts.editIndex)) {
					$this.datagrid('selectRow', opts.editIndex);
					return;
				}
				$this.datagrid('endEdit', opts.editIndex);
			}
			// 追加行
			row = $.extend({},row);
			$this.datagrid('appendRow', row);
			
			//处理只读字段,新增时可输
			var fields = $this.datagrid("getColumnFields");
			var readonlyCols = [];
			for(var i=0, len=fields.length; i<len; i++){
				var col = $this.datagrid("getColumnOption", fields[i]);
				if (col && typeof col.editor == "object") {
					if(col.editor.options && col.editor.options.readonly){
						readonlyCols.push(col);
						col.editor.options.readonly = false;
					}
				}
			}
			
			var rows = $this.datagrid('getRows');
			opts.editIndex = rows.length - 1;
			$this.datagrid('beginEdit', opts.editIndex);
			$this.datagrid('selectRow', opts.editIndex);
			
			//处理只读字段
			for(var i=0, len=readonlyCols.length; i<len; i++){
				readonlyCols[i].editor.options.readonly = true;
			}

			// 自动选中第一个单元格
			var editors = $this.datagrid('getEditors', opts.editIndex);
			if (editors.length) {
				editors[0].target.focus();
			}
		});
	},
	/**
	 * 保存记录
	 * 
	 * @param jq
	 * @param options : {
	 *            url : url,//必选项,后台处理URL queryName : "jsonStr",//可选项，后台接收的参数名称,默认为jsonStr params : null,//可选项,其他参数 showInfoSuccess : false,//操作成功后是否提示,默认为true success : function(){},//可选项,操作成功后的回调函数 fail : function(){}//可选项,操作失败后的回调函数 }
	 * 
	 * @returns
	 */
	saveRows : function(jq, options) {
		if (!options) {
			options = {};
		}

		return jq.each(function() {
			var $this = $(this);
			var opts = $.data(this, 'datagrid').options;

			// 如果正在编辑， 那儿验证正在编辑的行，如果验证通过则，完成编辑，否则返回。
			if (opts.editing && opts.editIndex >= 0) {
				if ($this.datagrid("validateRow", opts.editIndex)) {
					$this.datagrid('endEdit', opts.editIndex);
				} else {
					return;
				}
			}

			var jsonStr = $this.datagrid("getChangestr");
			if (jsonStr == "{inserted:[],updated:[],deleted:[]}") {
				$.messager.alert('提示', "数据未发生变化!", "info");
				return;
			}
			var url = opts.batchSaveUrl || options.url;
			var queryName = options.queryName ? options.queryName : "rows";
			var data = queryName + "=" + jsonStr;
			if (options.params) {
				data += options.params;
			}

			$.post(url, data, function(operInfo) {
				if (operInfo.isOk) {
					// 操作成功后是否提示
					var showInfoSuccess = (options.showInfoSuccess == false) ? false : true;
					if (showInfoSuccess) {
						$.messager.alert('提示', operInfo.info, "info");
					}
					$this.datagrid("reload");
					if (options.success) {
						options.success();
					}
				} else {
					$.messager.alert("提示", operInfo.info, "error");
					if (options.fail) {
						options.fail();
					}
				}
			}, 'json');
		});
	},
	// 获取修改记录的字符串
	getChangestr : function(jq) {
		var $this = jq;
		var options = $this.datagrid("options");
		if (options.editIndex >= 0) {
			$this.datagrid('endEdit', options.editIndex);
		}
		var insertRows = $this.datagrid("getChanges", "inserted");
		var updateRows = $this.datagrid("getChanges", "updated");
		var deleteRows = $this.datagrid("getChanges", "deleted");

		var rows = new Array();
		rows = rows.concat("{");
		rows = rows.concat("inserted:" + JSON.stringify(insertRows) + ",");
		rows = rows.concat("updated:" + JSON.stringify(updateRows) + ",");
		rows = rows.concat("deleted:" + JSON.stringify(deleteRows));
		rows = rows.concat("}");

		return rows.join("");
	},
	/**
	 * 使用如：
	 * $("#Datagrid").datagrid("delRow");        //默认提示确认框
	 * $("#Datagrid").datagrid("delRow",false);  //不提示确认框
	 * @param jq
	 * @param confirm 
	 * @returns
	 */
	delRow : function(jq, confirm) {
		return jq.each(function() {
			var $this = $(this);
			var row = $this.datagrid('getSelected');
			var index = $this.datagrid('getRowIndex', row);

			confirm = confirm ? confirm : true; //是否提示确认框 ，默认设置为true
			
			if (row) {
				if(confirm){
					$.messager.confirm('提示信息', '确定要删除吗?', function(r) {
						if (r) {
							$this.datagrid('deleteRow', index);
						}
					});
				} else {
					$this.datagrid('deleteRow', index);
				}
			} else {
				$.messager.alert('提示', "请选择要删除的记录!", "info");
			}
		});
	},
	deleteRows : function(jq){
		return jq.each(function() {
			var $this = $(this);
			
			var rows = $this.datagrid("getSelections");
			for(var i=0;i<rows.length;i++){
				var rowIndex = $this.datagrid("getRowIndex",rows[i]);
				$this.datagrid("deleteRow", rowIndex);
			}
		});
	},
	cancelRow : function(jq) {
		return jq.each(function() {
			var index = $(this).datagrid('options').editIndex;
			$(this).datagrid('cancelEdit', index);
		});
	}
});
