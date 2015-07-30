(function($) {
	function wrapTree(target) {
		var tree = $(target);
		tree.addClass("tree");
		return tree;
	}
	;
	//获取数据
	function getTreeData(target) {
		var list = [];
		collectTreeData(list, $(target));
		function collectTreeData(aa, target) {
			target.children("li").each(function() {
				var node = $(this);
				//modify in version-1.3
				var nodeData = $.extend({}, $.parser.parseOptions(this, [ "id", "iconCls", "state" ]), {
					checked : (node.attr("checked") ? true : undefined)
				});
				nodeData.text = node.children("span").html();
				if (!nodeData.text) {
					nodeData.text = node.html();
				}
				
				//子节点
				var children = node.children("ul");
				if (children.length) {
					nodeData.children = [];
					collectTreeData(nodeData.children, children);
				}
				aa.push(nodeData);
			});
		}
		;
		return list;
	}
	;
	function bindTreeEvents(target) {
		var opts = $.data(target, "tree").options;
		var tree = $.data(target, "tree").tree;
		$("div.tree-node", tree).unbind(".tree").bind("dblclick.tree", function() {
			select(target, this);
			opts.onDblClick.call(target, getSelected(target));
		}).bind("click.tree", function() {
			select(target, this);
			opts.onClick.call(target, getSelected(target));
		}).bind("mouseenter.tree", function() {
			$(this).addClass("tree-node-hover");
			return false;
		}).bind("mouseleave.tree", function() {
			$(this).removeClass("tree-node-hover");
			return false;
		}).bind("contextmenu.tree", function(e) {
			opts.onContextMenu.call(target, e, getNode(target, this));
		});
		$("span.tree-hit", tree).unbind(".tree").bind("click.tree", function() {
			var node = $(this).parent();
			toggle(target, node[0]);
			return false;
		}).bind("mouseenter.tree", function() {
			if ($(this).hasClass("tree-expanded")) {
				$(this).addClass("tree-expanded-hover");
			} else {
				$(this).addClass("tree-collapsed-hover");
			}
		}).bind("mouseleave.tree", function() {
			if ($(this).hasClass("tree-expanded")) {
				$(this).removeClass("tree-expanded-hover");
			} else {
				$(this).removeClass("tree-collapsed-hover");
			}
		}).bind("mousedown.tree", function() {
			return false;
		});
		$("span.tree-checkbox", tree).unbind(".tree").bind("click.tree", function() {
			var node = $(this).parent();
			setChecked(target, node[0], !$(this).hasClass("tree-checkbox1"));
			return false;
		}).bind("mousedown.tree", function() {
			return false;
		});
	}
	;
	function disableDnd(target) {
		var target = $(target).find("div.tree-node");
		target.draggable("disable");
		target.css("cursor", "pointer");
	}
	;
	function enableDnd(target) {
		var opts = $.data(target, "tree").options;
		var tree = $.data(target, "tree").tree;
		tree.find("div.tree-node").draggable({
			disabled : false,
			revert : true,
			cursor : "pointer",
			proxy : function(target) {
				var p = $("<div class=\"tree-node-proxy tree-dnd-no\"></div>").appendTo("body");
				p.html($(target).find(".tree-title").html());
				p.hide();
				return p;
			},
			deltaX : 15,
			deltaY : 15,
			onBeforeDrag : function(e) {
				if (e.which != 1) {
					return false;
				}
				$(this).next("ul").find("div.tree-node").droppable({
					accept : "no-accept"
				});
				//add in version-1.3
				var _1a = $(this).find("span.tree-indent");
				if (_1a.length) {
					e.data.startLeft += _1a.length * _1a.width();
				}
			},
			onStartDrag : function() {
				$(this).draggable("proxy").css({
					left : -10000,
					top : -10000
				});
			},
			onDrag : function(e) {
				var x1 = e.pageX, y1 = e.pageY, x2 = e.data.startX, y2 = e.data.startY;
				var d = Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
				if (d > 3) {
					$(this).draggable("proxy").show();
				}
				this.pageY = e.pageY;
			},
			onStopDrag : function() {
				$(this).next("ul").find("div.tree-node").droppable({
					accept : "div.tree-node"
				});
			}
		}).droppable({
			accept : "div.tree-node",
			onDragOver : function(e, source) {
				var pageY = source.pageY;
				var top = $(this).offset().top;
				var height = top + $(this).outerHeight();
				$(source).draggable("proxy").removeClass("tree-dnd-no").addClass("tree-dnd-yes");
				$(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
				if (pageY > top + (height - top) / 2) {
					if (height - pageY < 5) {
						$(this).addClass("tree-node-bottom");
					} else {
						$(this).addClass("tree-node-append");
					}
				} else {
					if (pageY - top < 5) {
						$(this).addClass("tree-node-top");
					} else {
						$(this).addClass("tree-node-append");
					}
				}
			},
			onDragLeave : function(e, source) {
				$(source).draggable("proxy").removeClass("tree-dnd-yes").addClass("tree-dnd-no");
				$(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
			},
			onDrop : function(e, source) {
				var target = this;
				var action, point;
				if ($(this).hasClass("tree-node-append")) {
					action = moveNode;
				} else {
					action = insertNode;
					point = $(this).hasClass("tree-node-top") ? "top" : "bottom";
				}
				setTimeout(function() {
					action(source, target, point);
				}, 0);
				$(this).removeClass("tree-node-append tree-node-top tree-node-bottom");
			}
		});
		function moveNode(nodeEl, parent) {
			if (getNode(target, parent).state == "closed") {
				expand(target, parent, function() {
					doMoveNode();
				});
			} else {
				doMoveNode();
			}
			function doMoveNode() {
				var nodeData = $(target).tree("pop", nodeEl);
				$(target).tree("append", {
					parent : parent,
					data : [ nodeData ]
				});
				opts.onDrop.call(target, parent, nodeData, "append");
			}
			;
		}
		;
		function insertNode(nodeEl, parent, point) {
			var param = {};
			if (point == "top") {
				param.before = parent;
			} else {
				param.after = parent;
			}
			var nodeData = $(target).tree("pop", nodeEl);
			param.data = nodeData;
			$(target).tree("insert", param);
			opts.onDrop.call(target, parent, nodeData, point);
		}
		;
	}
	;
	function setChecked(target, nodeEl, checked) {
		var opts = $.data(target, "tree").options;
		if (!opts.checkbox) {
			return;
		}
		var node = $(nodeEl);
		var ck = node.find(".tree-checkbox");
		ck.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
		if (checked) {
			ck.addClass("tree-checkbox1");
		} else {
			ck.addClass("tree-checkbox0");
		}
		if (opts.cascadeCheck) {
			setParentCheckbox(node,opts.cascadeParent);
			setChildCheckbox(node);
		}
		var nodeParam = getNode(target, nodeEl);
		opts.onCheck.call(target, nodeParam, checked);
		function setChildCheckbox(nodeEl) {
			var childck = nodeEl.next().find(".tree-checkbox");
			childck.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
			if (nodeEl.find(".tree-checkbox").hasClass("tree-checkbox1")) {
				childck.addClass("tree-checkbox1");
			} else {
				childck.addClass("tree-checkbox0");
			}
		}
		;
		function setParentCheckbox(nodeEl,cascadeParent) {
			var pNode = getParent(target, nodeEl[0]);
			if (pNode) {
				var ck = $(pNode.target).find(".tree-checkbox");
				ck.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
				
				//子节点全部unchecked ，则父节点为 unchecked
				if (isAllNull(nodeEl)) {
					ck.addClass("tree-checkbox0");
				//子节点全部checked 或者设置子节点 级联父节点模式 ，则父节点为 checked	
				} else if (cascadeParent || isAllSelected(nodeEl)) {
					ck.addClass("tree-checkbox1");
				//子节点未全部checked ,则父节点为方框状态	
				} else {
					ck.addClass("tree-checkbox2");
				};
				setParentCheckbox($(pNode.target),cascadeParent);
			}
			function isAllSelected(n) {
				var ck = n.find(".tree-checkbox");
				if (ck.hasClass("tree-checkbox0") || ck.hasClass("tree-checkbox2")) {
					return false;
				}
				var b = true;
				n.parent().siblings().each(function() {
					if (!$(this).children("div.tree-node").children(".tree-checkbox").hasClass("tree-checkbox1")) {
						b = false;
					}
				});
				return b;
			}
			;
			function isAllNull(n) {
				var ck = n.find(".tree-checkbox");
				if (ck.hasClass("tree-checkbox1") || ck.hasClass("tree-checkbox2")) {
					return false;
				}
				var b = true;
				n.parent().siblings().each(function() {
					if (!$(this).children("div.tree-node").children(".tree-checkbox").hasClass("tree-checkbox0")) {
						b = false;
					}
				});
				return b;
			}
			;
		}
		;
	}
	;
	function setCheckBoxValue(target, nodeEl) {
		var opts = $.data(target, "tree").options;
		var node = $(nodeEl);
		if (isLeaf(target, nodeEl)) {
			var ck = node.find(".tree-checkbox");
			if (ck.length) {
				if (ck.hasClass("tree-checkbox1")) {
					setChecked(target, nodeEl, true);
				} else {
					setChecked(target, nodeEl, false);
				}
			} else {
				if (opts.onlyLeafCheck) {
					$("<span class=\"tree-checkbox tree-checkbox0\"></span>").insertBefore(node.find(".tree-title"));
					bindTreeEvents(target);
				}
			}
		} else {
			var ck = node.find(".tree-checkbox");
			if (opts.onlyLeafCheck) {
				ck.remove();
			} else {
				if (ck.hasClass("tree-checkbox1")) {
					setChecked(target, nodeEl, true);
				} else {
					if (ck.hasClass("tree-checkbox2")) {
						var checked = true;
						var unchecked = true;
						var children = getChildren(target, nodeEl);
						for ( var i = 0; i < children.length; i++) {
							if (children[i].checked) {
								unchecked = false;
							} else {
								checked = false;
							}
						}
						if (checked) {
							setChecked(target, nodeEl, true);
						}
						if (unchecked) {
							setChecked(target, nodeEl, false);
						}
					}
				}
			}
		}
	}
	;
	function loadData(target, ul, data, isAppend) {
		var opts = $.data(target, "tree").options;
		data = opts.loadFilter.call(target, data, $(ul).prev("div.tree-node")[0]);
		if (!isAppend) {
			$(ul).empty();
		}
		var checkedNodes = [];
		var depth = $(ul).prev("div.tree-node").find("span.tree-indent, span.tree-hit").length;
		appendNodes(ul, data, depth);
		bindTreeEvents(target);
		if (opts.dnd) {
			enableDnd(target);
		} else {
			disableDnd(target);
		}
		for ( var i = 0; i < checkedNodes.length; i++) {
			setChecked(target, checkedNodes[i], true);
		}
		//add in version-1.3
		setTimeout(function() {
			_57(target, target);
		}, 0);
		var node = null;
		if (target != ul) {
			var prevNodes = $(ul).prev();
			node = getNode(target, prevNodes[0]);
		}
		opts.onLoadSuccess.call(target, node, data);
		function appendNodes(ul, children, depth) {
			for ( var i = 0; i < children.length; i++) {
				var li = $("<li></li>").appendTo(ul);
				var item = children[i];
				if (item.state != "open" && item.state != "closed") {
					item.state = "open";
				}
				var node = $("<div class=\"tree-node\"></div>").appendTo(li);
				node.attr("node-id", item.id);
				$.data(node[0], "tree-node", {
					id : item.id,
					text : item.text,
					iconCls : item.iconCls,
					attributes : item.attributes
				});
				$("<span class=\"tree-title\"></span>").html(item.text).appendTo(node);
				if (opts.checkbox) {
					if (opts.onlyLeafCheck) {
						if (item.state == "open" && (!item.children || !item.children.length)) {
							if (item.checked) {
								$("<span class=\"tree-checkbox tree-checkbox1\"></span>").prependTo(node);
							} else {
								$("<span class=\"tree-checkbox tree-checkbox0\"></span>").prependTo(node);
							}
						}
					} else {
						if (item.checked) {
							$("<span class=\"tree-checkbox tree-checkbox1\"></span>").prependTo(node);
							checkedNodes.push(node[0]);
						} else {
							$("<span class=\"tree-checkbox tree-checkbox0\"></span>").prependTo(node);
						}
					}
				}
				if (item.children && item.children.length) {
					var subul = $("<ul></ul>").appendTo(li);
					if (item.state == "open") {
						$("<span class=\"tree-icon tree-folder tree-folder-open\"></span>").addClass(item.iconCls).prependTo(node);
						$("<span class=\"tree-hit tree-expanded\"></span>").prependTo(node);
					} else {
						$("<span class=\"tree-icon tree-folder\"></span>").addClass(item.iconCls).prependTo(node);
						$("<span class=\"tree-hit tree-collapsed\"></span>").prependTo(node);
						subul.css("display", "none");
					}
					appendNodes(subul, item.children, depth + 1);
				} else {
					if (item.state == "closed") {
						$("<span class=\"tree-icon tree-folder\"></span>").addClass(item.iconCls).prependTo(node);
						$("<span class=\"tree-hit tree-collapsed\"></span>").prependTo(node);
					} else {
						$("<span class=\"tree-icon tree-file\"></span>").addClass(item.iconCls).prependTo(node);
						$("<span class=\"tree-indent\"></span>").prependTo(node);
					}
				}
				for ( var j = 0; j < depth; j++) {
					$("<span class=\"tree-indent\"></span>").prependTo(node);
				}
			}
		}
		;
	}
	;
	//add in version-1.3
	function _57(_58, ul, _59) {
		var _5a = $.data(_58, "tree").options;
		if (!_5a.lines) {
			return;
		}
		if (!_59) {
			_59 = true;
			$(_58).find("span.tree-indent").removeClass("tree-line tree-join tree-joinbottom");
			$(_58).find("div.tree-node").removeClass("tree-node-last tree-root-first tree-root-one");
			var _5b = $(_58).tree("getRoots");
			if (_5b.length > 1) {
				$(_5b[0].target).addClass("tree-root-first");
			} else {
				$(_5b[0].target).addClass("tree-root-one");
			}
		}
		$(ul).children("li").each(function() {
			var _5c = $(this).children("div.tree-node");
			var ul = _5c.next("ul");
			if (ul.length) {
				if ($(this).next().length) {
					_5d(_5c);
				}
				_57(_58, ul, _59);
			} else {
				_5e(_5c);
			}
		});
		var _5f = $(ul).children("li:last").children("div.tree-node").addClass("tree-node-last");
		_5f.children("span.tree-join").removeClass("tree-join").addClass("tree-joinbottom");
		function _5e(_60, _61) {
			var _62 = _60.find("span.tree-icon");
			_62.prev("span.tree-indent").addClass("tree-join");
		}
		;
		function _5d(_63) {
			var _64 = _63.find("span.tree-indent, span.tree-hit").length;
			_63.next().find("div.tree-node").each(function() {
				$(this).children("span:eq(" + (_64 - 1) + ")").addClass("tree-line");
			});
		}
		;
	}
	;
	function request(target, ul, param, callBack) {
		var opts = $.data(target, "tree").options;
		param = param || {};
		var node = null;
		if (target != ul) {
			var prev = $(ul).prev();
			node = getNode(target, prev[0]);
		}
		if (opts.onBeforeLoad.call(target, node, param) == false) {
			return;
		}
		//modify in version-1.3
		var _6c = $(ul).prev().children("span.tree-folder");
		_6c.addClass("tree-loading");
		var _6d = opts.loader.call(target, param, function(_6e) {
			_6c.removeClass("tree-loading");
			loadData(target, ul, _6e);
			if (callBack) {
				callBack();
			}
		}, function() {
			_6c.removeClass("tree-loading");
			opts.onLoadError.apply(target, arguments);
			if (callBack) {
				callBack();
			}
		});
		if (_6d == false) {
			_6c.removeClass("tree-loading");
		}
	}
	;
	function expand(target, nodeEl, callBack) {
		var opts = $.data(target, "tree").options;
		var hit = $(nodeEl).children("span.tree-hit");
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-expanded")) {
			return;
		}
		var node = getNode(target, nodeEl);
		if (opts.onBeforeExpand.call(target, node) == false) {
			return;
		}
		hit.removeClass("tree-collapsed tree-collapsed-hover").addClass("tree-expanded");
		hit.next().addClass("tree-folder-open");
		var ul = $(nodeEl).next();
		if (ul.length) {
			if (opts.animate) {
				ul.slideDown("normal", function() {
					opts.onExpand.call(target, node);
					if (callBack) {
						callBack();
					}
				});
			} else {
				ul.css("display", "block");
				opts.onExpand.call(target, node);
				if (callBack) {
					callBack();
				}
			}
		} else {
			var newNode = $("<ul style=\"display:none\"></ul>").insertAfter(nodeEl);
			request(target, newNode[0], {
				id : node.id
			}, function() {
				if (newNode.is(":empty")) {
					newNode.remove();
				}
				if (opts.animate) {
					newNode.slideDown("normal", function() {
						opts.onExpand.call(target, node);
						if (callBack) {
							callBack();
						}
					});
				} else {
					newNode.css("display", "block");
					opts.onExpand.call(target, node);
					if (callBack) {
						callBack();
					}
				}
			});
		}
	}
	;
	function collapse(target, nodeEl) {
		var opts = $.data(target, "tree").options;
		var hit = $(nodeEl).children("span.tree-hit");
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-collapsed")) {
			return;
		}
		var node = getNode(target, nodeEl);
		if (opts.onBeforeCollapse.call(target, node) == false) {
			return;
		}
		hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
		hit.next().removeClass("tree-folder-open");
		var ul = $(nodeEl).next();
		if (opts.animate) {
			ul.slideUp("normal", function() {
				opts.onCollapse.call(target, node);
			});
		} else {
			ul.css("display", "none");
			opts.onCollapse.call(target, node);
		}
	}
	;
	function toggle(target, nodeEl) {
		var hit = $(nodeEl).children("span.tree-hit");
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-expanded")) {
			collapse(target, nodeEl);
		} else {
			expand(target, nodeEl);
		}
	}
	;
	function expandAll(target, nodeEl) {
		var children = getChildren(target, nodeEl);
		if (nodeEl) {
			children.unshift(getNode(target, nodeEl));
		}
		for ( var i = 0; i < children.length; i++) {
			expand(target, children[i].target);
		}
	}
	;
	function expandTo(target, nodeEl) {
		var ancestors = [];
		var p = getParent(target, nodeEl);
		while (p) {
			ancestors.unshift(p);
			p = getParent(target, p.target);
		}
		for ( var i = 0; i < ancestors.length; i++) {
			expand(target, ancestors[i].target);
		}
	}
	;
	function collapseAll(target, nodeEl) {
		var children = getChildren(target, nodeEl);
		if (nodeEl) {
			children.unshift(getNode(target, nodeEl));
		}
		for ( var i = 0; i < children.length; i++) {
			collapse(target, children[i].target);
		}
	}
	;
	function getRoot(target) {
		var roots = getRoots(target);
		if (roots.length) {
			return roots[0];
		} else {
			return null;
		}
	}
	;
	function getRoots(target) {
		var roots = [];
		$(target).children("li").each(function() {
			var node = $(this).children("div.tree-node");
			roots.push(getNode(target, node[0]));
		});
		return roots;
	}
	;
	function getChildren(target, nodeEl) {
		var children = [];
		if (nodeEl) {
			findChildren($(nodeEl));
		} else {
			var roots = getRoots(target);
			for ( var i = 0; i < roots.length; i++) {
				children.push(roots[i]);
				findChildren($(roots[i].target));
			}
		}
		function findChildren(node) {
			node.next().find("div.tree-node").each(function() {
				children.push(getNode(target, this));
			});
		}
		;
		return children;
	}
	;
	function getParent(target, nodeEl) {
		var ul = $(nodeEl).parent().parent();
		if (ul[0] == target) {
			return null;
		} else {
			return getNode(target, ul.prev()[0]);
		}
	}
	;
	function getChecked(target) {
		var checkedNodes = [];
		$(target).find(".tree-checkbox1").each(function() {
			var node = $(this).parent();
			checkedNodes.push(getNode(target, node[0]));
		});
		return checkedNodes;
	}
	;
	function getSelected(target) {
		var node = $(target).find("div.tree-node-selected");
		if (node.length) {
			return getNode(target, node[0]);
		} else {
			return null;
		}
	}
	;
	function append(target, param) {
		var node = $(param.parent);
		var ul;
		if (node.length == 0) {
			ul = $(target);
		} else {
			ul = node.next();
			if (ul.length == 0) {
				ul = $("<ul></ul>").insertAfter(node);
			}
		}
		if (param.data && param.data.length) {
			var icon = node.find("span.tree-icon");
			if (icon.hasClass("tree-file")) {
				icon.removeClass("tree-file").addClass("tree-folder");
				var hit = $("<span class=\"tree-hit tree-expanded\"></span>").insertBefore(icon);
				if (hit.prev().length) {
					hit.prev().remove();
				}
			}
		}
		loadData(target, ul[0], param.data, true);
		
		//子节点关联父节点模式下，不再重新渲染父节点的级联
		var opts = $.data(target, "tree").options;
		if(!opts.cascadeParent){
			setCheckBoxValue(target, ul.prev());
		}
	}
	;
	function insert(target, param) {
		var ref = param.before || param.after;
		var pNode = getParent(target, ref);
		var li;
		if (pNode) {
			append(target, {
				parent : pNode.target,
				data : [ param.data ]
			});
			li = $(pNode.target).next().children("li:last");
		} else {
			append(target, {
				parent : null,
				data : [ param.data ]
			});
			li = $(target).children("li:last");
		}
		if (param.before) {
			li.insertBefore($(ref).parent());
		} else {
			li.insertAfter($(ref).parent());
		}
	}
	;
	function remove(target, nodeEl) {
		var pNode = getParent(target, nodeEl);
		var node = $(nodeEl);
		var li = node.parent();
		var ul = li.parent();
		li.remove();
		if (ul.children("li").length == 0) {
			var node = ul.prev();
			node.find(".tree-icon").removeClass("tree-folder").addClass("tree-file");
			node.find(".tree-hit").remove();
			$("<span class=\"tree-indent\"></span>").prependTo(node);
			if (ul[0] != target) {
				ul.remove();
			}
		}
		if (pNode) {
			setCheckBoxValue(target, pNode.target);
		}
		//add in version-1.3
		_57(target, target);
	}
	;
	function getData(target, nodeEl) {
		function getChildren(aa, ul) {
			ul.children("li").each(function() {
				var nodes = $(this).children("div.tree-node");
				var node = getNode(target, nodes[0]);
				var sub = $(this).children("ul");
				if (sub.length) {
					node.children = [];
					getData(node.children, sub);
				}
				aa.push(_11a);
			});
		}
		;
		if (nodeEl) {
			var node = getNode(target, nodeEl);
			node.children = [];
			getChildren(node.children, $(nodeEl).next());
			return node;
		} else {
			return null;
		}
	}
	;
	function update(target, param) {
		var node = $(param.target);
		var data = getNode(target, param.target);
		if (data.iconCls) {
			node.find(".tree-icon").removeClass(data.iconCls);
		}
		var _bb = $.extend({}, data, param);
		$.data(_b8.target, "tree-node", _bb);
		_b9.attr("node-id", _bb.id);
		_b9.find(".tree-title").html(_bb.text);
		if (_bb.iconCls) {
			_b9.find(".tree-icon").addClass(_bb.iconCls);
		}
		if (data.checked != _bb.checked) {
			setChecked(target, param.target, _bb.checked);
		}
	}
	;
	function getNode(target, nodeEl) {
		var node = $.extend({}, $.data(nodeEl, "tree-node"), {
			target : nodeEl,
			checked : $(nodeEl).find(".tree-checkbox").hasClass("tree-checkbox1")
		});
		if (!isLeaf(target, nodeEl)) {
			node.state = $(nodeEl).find(".tree-hit").hasClass("tree-expanded") ? "open" : "closed";
		}
		return node;
	}
	;
	function find(target, id) {
		var node = $(target).find("div.tree-node[node-id=" + id + "]");
		if (node.length) {
			return getNode(target, node[0]);
		} else {
			return null;
		}
	}
	;
	function select(target, nodeEl) {
		var opts = $.data(target, "tree").options;
		var node = getNode(target, nodeEl);
		if (opts.onBeforeSelect.call(target, node) == false) {
			return;
		}
		$("div.tree-node-selected", target).removeClass("tree-node-selected");
		$(nodeEl).addClass("tree-node-selected");
		opts.onSelect.call(target, node);
	}
	;
	function isLeaf(target, nodeEl) {
		var node = $(nodeEl);
		var hit = node.children("span.tree-hit");
		return hit.length == 0;
	}
	;
	function beginEdit(target, nodeEl) {
		var opts = $.data(target, "tree").options;
		var node = getNode(target, nodeEl);
		if (opts.onBeforeEdit.call(target, node) == false) {
			return;
		}
		$(nodeEl).css("position", "relative");
		var nt = $(nodeEl).find(".tree-title");
		var outerWidth = nt.outerWidth();
		nt.empty();
		var editor = $("<input class=\"tree-editor\">").appendTo(nt);
		editor.val(node.text).focus();
		editor.width(outerWidth + 20);
		editor.height(document.compatMode == "CSS1Compat" ? (18 - (editor.outerHeight() - editor.height())) : 18);
		editor.bind("click", function(e) {
			return false;
		}).bind("mousedown", function(e) {
			e.stopPropagation();
		}).bind("mousemove", function(e) {
			e.stopPropagation();
		}).bind("keydown", function(e) {
			if (e.keyCode == 13) {
				endEdit(target, nodeEl);
				return false;
			} else {
				if (e.keyCode == 27) {
					cancelEdit(target, nodeEl);
					return false;
				}
			}
		}).bind("blur", function(e) {
			e.stopPropagation();
			endEdit(target, nodeEl);
		});
	}
	;
	function endEdit(target, nodeEl) {
		var opts = $.data(target, "tree").options;
		$(nodeEl).css("position", "");
		var editor = $(nodeEl).find("input.tree-editor");
		var val = editor.val();
		editor.remove();
		var node = getNode(target, nodeEl);
		node.text = val;
		update(target, node);
		opts.onAfterEdit.call(target, node);
	}
	;
	function cancelEdit(target, nodeEl) {
		var opts = $.data(target, "tree").options;
		$(nodeEl).css("position", "");
		$(nodeEl).find("input.tree-editor").remove();
		var node = getNode(target, nodeEl);
		update(target, node);
		opts.onCancelEdit.call(target, node);
	}
	;
	$.fn.tree = function(options, param) {
		if (typeof options == "string") {
			return $.fn.tree.methods[options](this, param);
		}
		var options = options || {};
		return this.each(function() {
			var state = $.data(this, "tree");
			var opts;
			if (state) {
				opts = $.extend(state.options, options);
				state.options = opts;
			} else {
				opts = $.extend({}, $.fn.tree.defaults, $.fn.tree.parseOptions(this), options);
				$.data(this, "tree", {
					options : opts,
					tree : wrapTree(this)
				});
				var data = getTreeData(this);
				if (data.length && !opts.data) {
					opts.data = data;
				}
			}
			if (opts.lines) {
				$(this).addClass("tree-lines");
			}
			if (opts.data) {
				loadData(this, this, opts.data);
			} else {
				if (opts.dnd) {
					enableDnd(this);
				} else {
					disableDnd(this);
				}
			}
			request(this, this);
		});
	};
	$.fn.tree.methods = {
		options : function(jq) {
			return $.data(jq[0], "tree").options;
		},
		loadData : function(jq, data) {
			return jq.each(function() {
				loadData(this, this, data);
			});
		},
		getNode : function(jq, target) {
			return getNode(jq[0], target);
		},
		getData : function(jq, target) {
			return getData(jq[0], target);
		},
		reload : function(jq, target) {
			return jq.each(function() {
				if (target) {
					var node = $(target);
					var hit = node.children("span.tree-hit");
					hit.removeClass("tree-expanded tree-expanded-hover").addClass("tree-collapsed");
					node.next().remove();
					expand(this, target);
				} else {
					$(this).empty();
					request(this, this);
				}
			});
		},
		getRoot : function(jq) {
			return getRoot(jq[0]);
		},
		getRoots : function(jq) {
			return getRoots(jq[0]);
		},
		getParent : function(jq, target) {
			return getParent(jq[0], target);
		},
		getChildren : function(jq, target) {
			return getChildren(jq[0], target);
		},
		getChecked : function(jq) {
			return getChecked(jq[0]);
		},
		getSelected : function(jq) {
			return getSelected(jq[0]);
		},
		isLeaf : function(jq, target) {
			return isLeaf(jq[0], target);
		},
		find : function(jq, id) {
			return find(jq[0], id);
		},
		select : function(jq, target) {
			return jq.each(function() {
				select(this, target);
			});
		},
		check : function(jq, target) {
			return jq.each(function() {
				setChecked(this, target, true);
			});
		},
		uncheck : function(jq, target) {
			return jq.each(function() {
				setChecked(this, target, false);
			});
		},
		collapse : function(jq, target) {
			return jq.each(function() {
				collapse(this, target);
			});
		},
		expand : function(jq, target) {
			return jq.each(function() {
				expand(this, target);
			});
		},
		collapseAll : function(jq, target) {
			return jq.each(function() {
				collapseAll(this, target);
			});
		},
		expandAll : function(jq, target) {
			return jq.each(function() {
				expandAll(this, target);
			});
		},
		expandTo : function(jq, target) {
			return jq.each(function() {
				expandTo(this, target);
			});
		},
		toggle : function(jq, target) {
			return jq.each(function() {
				toggle(this, target);
			});
		},
		append : function(jq, target) {
			return jq.each(function() {
				append(this, target);
			});
		},
		insert : function(jq, target) {
			return jq.each(function() {
				insert(this, target);
			});
		},
		remove : function(jq, target) {
			return jq.each(function() {
				remove(this, target);
			});
		},
		pop : function(jq, target) {
			var node = jq.tree("getData", target);
			jq.tree("remove", _149);
			return node;
		},
		update : function(jq, target) {
			return jq.each(function() {
				update(this, target);
			});
		},
		enableDnd : function(jq) {
			return jq.each(function() {
				enableDnd(this);
			});
		},
		disableDnd : function(jq) {
			return jq.each(function() {
				disableDnd(this);
			});
		},
		beginEdit : function(jq, target) {
			return jq.each(function() {
				beginEdit(this, target);
			});
		},
		endEdit : function(jq, target) {
			return jq.each(function() {
				endEdit(this, target);
			});
		},
		cancelEdit : function(jq, target) {
			return jq.each(function() {
				cancelEdit(this, target);
			});
		}
	};
	$.fn.tree.parseOptions = function(_fb) {
		var t = $(_fb);
		return $.extend({}, $.parser.parseOptions(_fb, [ "url", "method", {
			checkbox : "boolean",
			cascadeCheck : "boolean",
			casecadeParent : "boolean",
			onlyLeafCheck : "boolean"
		}, {
			animate : "boolean",
			lines : "boolean",
			dnd : "boolean"
		} ]));
	};
	$.fn.tree.defaults = {
		url : null,
		method : "post",
		animate : false,
		checkbox : false,
		cascadeCheck : true,
		cascadeParent : false,
		onlyLeafCheck : false,
		lines : false,
		dnd : false,
		data : null,
		loader : function(param, success, error) {
			var opts = $(this).tree("options");
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
		loadFilter : function(data, _100) {
			return data;
		},
		onBeforeLoad : function(node, _101) {
		},
		onLoadSuccess : function(node, data) {
		},
		onLoadError : function() {
		},
		onClick : function(node) {
		},
		onDblClick : function(node) {
		},
		onBeforeExpand : function(node) {
		},
		onExpand : function(node) {
		},
		onBeforeCollapse : function(node) {
		},
		onCollapse : function(node) {
		},
		onCheck : function(node, checked) {
		},
		onBeforeSelect : function(node) {
		},
		onSelect : function(node) {
		},
		onContextMenu : function(e, node) {
		},
		onDrop : function(target, source, point) {
		},
		onBeforeEdit : function(node) {
		},
		onAfterEdit : function(node) {
		},
		onCancelEdit : function(node) {
		}
	};
})(jQuery);