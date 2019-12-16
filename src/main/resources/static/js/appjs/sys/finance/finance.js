
var prefix = "/sys/finance"
$(function() {
	load();
});

function load() {
	$('#exampleTable')
			.bootstrapTable(
					{
						method : 'get', // 服务器数据的请求方式 get or post
						url : prefix + "/list", // 服务器数据的加载地址
						showRefresh : true,
						showToggle : true,
						showColumns : true,
						showExport: true,
						exportTypes: ['excel','xlsx'],
						iconSize : 'outline',
						toolbar : '#exampleToolbar',
						striped : true, // 设置为true会有隔行变色效果
						dataType : "json", // 服务器返回的数据类型
						pagination : true, // 设置为true会在底部显示分页条
						// queryParamsType : "limit",
						// //设置为limit则会发送符合RESTFull格式的参数
						singleSelect : false, // 设置为true将禁止多选
						// contentType : "application/x-www-form-urlencoded",
						// //发送到服务器的数据编码类型
						pageSize : 10, // 如果设置了分页，每页数据条数
						pageNumber : 1, // 如果设置了分布，首页页码
						//search : true, // 是否显示搜索框
						showColumns : true, // 是否显示内容下拉框（选择显示的列）
						sidePagination : "server", // 设置在哪里进行分页，可选值为"client" 或者 "server"
						queryParams : function(params) {
							return {
								//说明：传入后台的参数包括offset开始索引，limit步长，sort排序列，order：desc或者,以及所有列的键值对
								limit: params.limit,
								offset:params.offset,
                                parkingId: $('#parkingId').val(),
								startDate: $("#startDate").val(),
								finalDate: $("#finalDate").val(),
								carNumber: '%' + $("#carNumber").val() + '%'
							};
						},
						// //请求服务器数据时，你可以通过重写参数的方式添加一些额外的参数，例如 toolbar 中的参数 如果
						// queryParamsType = 'limit' ,返回参数必须包含
						// limit, offset, search, sort, order 否则, 需要包含:
						// pageSize, pageNumber, searchText, sortName,
						// sortOrder.
						// 返回false将会终止请求
						columns : [
																{
									field : 'parkingName',
									title : '停车场名称' 
								},
																{
									field : 'carNumber',
									title : '车牌号'
								},
																{
									field : 'cost',
									title : '停车费',
									align : 'center'
								},
								{
									field : 'costTime',
									title : '停车时长（分钟）',
									align : 'center'
								},
																{
									field : 'payType',
									title : '缴费类型',
									align : 'center',
									//0：线下 1：微信 2：支付宝 3:vip 4：月租车、5：商户车辆
									formatter : function(value, row, index) {
										if (value == '0') {
											return '<span class="label label-danger">线下支付</span>';
										} else if (value == '1') {
											return '<span class="label label-success">微信支付</span>';
										} else if (value == '2') {
											return '<span class="label label-success">支付宝支付</span>';
										} else if (value == '3') {
											return '<span class="label label-primary">VIP</span>';
										}else if (value == '4') {
											return '<span class="label label-warning">月租车</span>';
										}else if (value == '5') {
											return '<span class="label label-info">商户车辆</span>';
										}else if (value == '6') {
											return '<span class="label label-default">无需支付</span>';
										}
									}
								},
																{
									field : 'entranceName',
									title : '车辆入口'
								},
																{
									field : 'exitName',
									title : '车辆出口'
								},
																{
									field : 'inTime',
									title : '进场时间'
								},
																{
									field : 'outTime',
									title : '出场时间'
								},
								{
									field : 'status',
									title : '是否出厂',
									align : 'center',
									formatter : function(value, row, index) {
										if (value == '0') {
											return '<span class="label label-danger">否</span>';
										} else if (value == '1') {
											return '<span class="label label-success">是</span>';
										} else if (value == '2') {
											return '<span class="label label-danger">待出场</span>';
										}
									}
								},
								{
									field : 'payStatus',
									title : '是否支付',
									align : 'center',
									formatter : function(value, row, index) {
										if (value == '0') {
											return '<span class="label label-danger">未支付</span>';
										} else if (value == '1') {
											return '<span class="label label-success">已支付</span>';
										}
									}
								}]
					});
}
function reLoad() {
	$('#exampleTable').bootstrapTable('refresh');
}
function add() {
	layer.open({
		type : 2,
		title : '增加',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/add' // iframe的url
	});
}
function edit(id) {
	layer.open({
		type : 2,
		title : '编辑',
		maxmin : true,
		shadeClose : false, // 点击遮罩关闭层
		area : [ '800px', '520px' ],
		content : prefix + '/edit/' + id // iframe的url
	});
}
function remove(id) {
	layer.confirm('确定要删除选中的记录？', {
		btn : [ '确定', '取消' ]
	}, function() {
		$.ajax({
			url : prefix+"/remove",
			type : "post",
			data : {
				'id' : id
			},
			success : function(r) {
				if (r.code==0) {
					layer.msg(r.msg);
					reLoad();
				}else{
					layer.msg(r.msg);
				}
			}
		});
	})
}

function resetPwd(id) {
}
function batchRemove() {
	var rows = $('#exampleTable').bootstrapTable('getSelections'); // 返回所有选择的行，当没有选择的记录时，返回一个空数组
	if (rows.length == 0) {
		layer.msg("请选择要删除的数据");
		return;
	}
	layer.confirm("确认要删除选中的'" + rows.length + "'条数据吗?", {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function() {
		var ids = new Array();
		// 遍历所有选择的行数据，取每条数据对应的ID
		$.each(rows, function(i, row) {
			ids[i] = row['id'];
		});
		$.ajax({
			type : 'POST',
			data : {
				"ids" : ids
			},
			url : prefix + '/batchRemove',
			success : function(r) {
				if (r.code == 0) {
					layer.msg(r.msg);
					reLoad();
				} else {
					layer.msg(r.msg);
				}
			}
		});
	}, function() {

	});
}