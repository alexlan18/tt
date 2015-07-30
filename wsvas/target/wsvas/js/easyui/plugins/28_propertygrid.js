(function($) {
	function init(jq) {
		var opts = $.data(jq, "propertygrid").options;
		$(jq).datagrid($.extend({}, opts, {
			cls : "propertygrid",
			view : (opts.showGroup ? view : undefined),
			onClickRow : function(rowIndex, rowData) {
				if (opts.editIndex != rowIndex&& rowData.editor) {
					var valueOpts = $(this).datagrid("getColumnOption", "value");
					valueOpts.editor = rowData.editor;
					stopEdit(opts.editIndex);
					$(this).datagrid("beginEdit", rowIndex);
					$(this).datagrid("getEditors", rowIndex)[0].target.focus();
					opts.editIndex = rowIndex;
				}
				opts.onClickRow.call(jq, rowIndex, rowData);
			},
			onLoadSuccess : function(data) {
				$(jq).datagrid("getPanel").find("div.datagrid-group").css("border", "0");
				opts.onLoadSuccess.call(jq, data);
			}
		}));
		$(jq).datagrid("getPanel").find("div.datagrid-body").unbind(".propertygrid").bind("mousedown.propertygrid", function(e) {
			e.stopPropagation();
		});
		$(document).unbind(".propertygrid").bind("mousedown.propertygrid", function() {
			stopEdit(opts.editIndex);
			opts.editIndex = undefined;
		});
		function stopEdit(rowIndex) {
			if (rowIndex == undefined) {
				return;
			}
			var t = $(jq);
			t.datagrid("getEditors",rowIndex)[0].target.blur();
			if (t.datagrid("validateRow", rowIndex)) {
				t.datagrid("endEdit", rowIndex);
			} else {
				t.datagrid("cancelEdit", rowIndex);
			}
		}
		;
	}
	;
	$.fn.propertygrid = function(options, param) {
		if (typeof options == "string") {
			var method = $.fn.propertygrid.methods[options];
			if (method) {
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}
		options = options || {};
		return this.each(function() {
			var state = $.data(this, "propertygrid");
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, "propertygrid", {
					options : $.extend({}, $.fn.propertygrid.defaults, $.fn.propertygrid.parseOptions(this), options)
				});
			}
			init(this);
		});
	};
	$.fn.propertygrid.methods = {};
	$.fn.propertygrid.parseOptions = function(target) {
		var t = $(target);
		return $.extend({}, $.fn.datagrid.parseOptions(target),$.parser.parseOptions(target, [ {
			showGroup : "boolean"
		} ]));
	};
	var view = $.extend({}, $.fn.datagrid.defaults.view, {
		render : function(jq, container, frozen) {
			var state = $.data(target, "datagrid");
			var opts = state.options;
			var rows = state.data.rows;
			var fields = $(jq).datagrid("getColumnFields", frozen);
			var html = [];
			var rowIndex = 0;
			var groups = this.groups;
			for ( var i = 0; i < groups.length; i++) {
				var group = groups[i];
				html.push("<div class=\"datagrid-group\" group-index=" + i + " style=\"height:25px;overflow:hidden;border-bottom:1px solid #ccc;\">");
				html.push("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\" style=\"height:100%\"><tbody>");
				html.push("<tr>");
				html.push("<td style=\"border:0;\">");
				if (!frozen) {
					html.push("<span style=\"color:#666;font-weight:bold;\">");
					html.push(opts.groupFormatter.call(jq, group.fvalue, group.rows));
					html.push("</span>");
				}
				html.push("</td>");
				html.push("</tr>");
				html.push("</tbody></table>");
				html.push("</div>");
				html.push("<table class=\"datagrid-btable\"  cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>");
				for ( var j = 0; j < group.rows.length; j++) {
					var cls = (rowIndex % 2 && opts.striped) ? "class=\"datagrid-row-alt\"" : "class=\"datagrid-row\"";
					var style = opts.rowStyler ? opts.rowStyler.call(jq, rowIndex, group.rows[j]) : "";
					var style = style ? "style=\"" + style + "\"" : "";
					var id = state.rowIdPrefix + "-" + (frozen ? 1 : 2) + "-" + rowIndex;
					html.push("<tr  id=\"" + id + "\" datagrid-row-index=\"" + rowIndex + "\" " + cls + " " + style + ">");
					html.push(this.renderRow.call(this, jq, fields, frozen, rowIndex, group.rows[j]));
					html.push("</tr>");
					rowIndex++;
				}
				html.push("</tbody></table>");
			}
			$(container).html(html.join(""));
		},
		onAfterRender : function(jq) {
			var opts = $.data(jq, "datagrid").options;
			var dc = $.data(jq, "datagrid").dc;
			var gridView = dc.view;
			var gridView1 = dc.view1;
			var gridView2 = dc.view2;
			$.fn.datagrid.defaults.view.onAfterRender.call(this, jq);
			if (opts.rownumbers || opts.frozenColumns.length) {
				var gridGroup = gridView1.find("div.datagrid-group");
			} else {
				var gridGroup = gridView2.find("div.datagrid-group");
			}
			$("<td style=\"border:0\"><div class=\"datagrid-row-expander datagrid-row-collapse\" style=\"width:25px;height:16px;cursor:pointer\"></div></td>").insertBefore(gridGroup.find("td"));
			gridView.find("div.datagrid-group").each(function() {
				var groupIndex = $(this).attr("group-index");
				$(this).find("div.datagrid-row-expander").bind("click", {
					groupIndex : groupIndex
				}, function(e) {
					if ($(this).hasClass("datagrid-row-collapse")) {
						$(jq).datagrid("collapseGroup", e.data.groupIndex);
					} else {
						$(jq).datagrid("expandGroup", e.data.groupIndex);
					}
				});
			});
		},
		onBeforeRender : function(jq, rows) {
			var opts = $.data(jq, "datagrid").options;
			var groups = [];
			for ( var i = 0; i < rows.length; i++) {
				var row = rows[i];
				var group = findGroup(row[opts.groupField]);
				if (!group) {
					group = {
						fvalue : row[opts.groupField],
						rows : [ row ],
						startRow : i
					};
					groups.push(group);
				} else {
					group.rows.push(row);
				}
			}
			function findGroup(value) {
				for ( var i = 0; i < groups.length; i++) {
					var group = groups[i];
					if (group.fvalue == value) {
						return group;
					}
				}
				return null;
			}
			;
			this.groups = groups;
			var newRows = [];
			for ( var i = 0; i < groups.length; i++) {
				var group = groups[i];
				for ( var j = 0; j < group.rows.length; j++) {
					newRows.push(group.rows[j]);
				}
			}
			$.data(jq, "datagrid").data.rows = newRows;
		}
	});
	$.extend($.fn.datagrid.methods, {
		expandGroup : function(jq, groupIndex) {
			return jq.each(function() {
				var gridView = $.data(this, "datagrid").dc.view;
				if (groupIndex != undefined) {
					var groups = gridView.find("div.datagrid-group[group-index=\"" + groupIndex + "\"]");
				} else {
					var groups = gridView.find("div.datagrid-group");
				}
				var rowExpander = groups.find("div.datagrid-row-expander");
				if (rowExpander.hasClass("datagrid-row-expand")) {
					rowExpander.removeClass("datagrid-row-expand").addClass("datagrid-row-collapse");
					groups.next("table").show();
				}
				$(this).datagrid("fixRowHeight");
			});
		},
		collapseGroup : function(jq, groupIndex) {
			return jq.each(function() {
				var gridView = $.data(this, "datagrid").dc.view;
				if (groupIndex != undefined) {
					var groups = gridView.find("div.datagrid-group[group-index=\"" + groupIndex + "\"]");
				} else {
					var groups = gridView.find("div.datagrid-group");
				}
				var rowExpander = groups.find("div.datagrid-row-expander");
				if (rowExpander.hasClass("datagrid-row-collapse")) {
					rowExpander.removeClass("datagrid-row-collapse").addClass("datagrid-row-expand");
					groups.next("table").hide();
				}
				$(this).datagrid("fixRowHeight");
			});
		}
	});
	$.fn.propertygrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		singleSelect : true,
		remoteSort : false,
		fitColumns : true,
		loadMsg : "",
		frozenColumns : [ [ {
			field : "f",
			width : 16,
			resizable : false
		} ] ],
		columns : [ [ {
			field : "name",
			title : "Name",
			width : 100,
			sortable : true
		}, {
			field : "value",
			title : "Value",
			width : 100,
			resizable : false
		} ] ],
		showGroup : false,
		groupField : "group",
		groupFormatter : function(group,rows) {
			return group;
		}
	});
})(jQuery);
