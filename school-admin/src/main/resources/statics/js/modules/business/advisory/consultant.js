$(function () {
    $("#jqGrid").jqGrid({
        url: baseBusinessURL + 'advisory/consultant/list',
        datatype: "json",
        colModel: [			
			{ label: '咨询师ID', name: 'id', index: "id", width: 45, key: true },
			{ label: '咨询师照片', name: 'photoUrl', width: 75 },
			{ label: '座右铭', name: 'motto', width: 90 },
			{ label: '咨询师名字', name: 'consultantName', width: 100 },
			{ label: '职称', name: 'jobTitle', width: 100 },
			{ label: '咨询师类型', name: 'consultantType', width: 100 },
			{ label: '咨询师等级', name: 'consultantLevel', width: 100 },
			{ label: '状态', name: 'delFlag', width: 60, formatter: function(value, options, row){
				return value === "0" ?
					'<span class="label label-success">正常</span>' :
					'<span class="label label-danger">禁用</span>';
			}},
			{ label: '创建时间', name: 'createTime', index: "create_time", width: 85}
        ],
		viewrecords: true,
        height: 385,
        rowNum: 10,
		rowList : [10,30,50],
        rownumbers: true, 
        rownumWidth: 25, 
        autowidth:true,
        multiselect: true,
        pager: "#jqGridPager",
        jsonReader : {
            root: "page.list",
            page: "page.currPage",
            total: "page.totalPage",
            records: "page.totalCount"
        },
        prmNames : {
            page:"pageNum",
            rows:"pageSize",
            order: "order"
        },
        gridComplete:function(){
        	//隐藏grid底部滚动条
        	$("#jqGrid").closest(".ui-jqgrid-bdiv").css({ "overflow-x" : "hidden" }); 
        }
    });
});

var vm = new Vue({
    el:'#rrapp',
    data:{
        q:{
        	consultantName:null
        },
        showList: true,
        title:null,
        consultant:{
            id:null,
            userpId:null,
            photoUrl:null,
            motto:null,
            consultantName:null,
            jobTitle:null,
            consultantType:null,
            consultantLevel:null,
            delFlag:"0"
        }
    },
    methods: {
        query: function () {
            vm.reload();
        },
        add: function(){
            vm.showList = false;
            vm.title = "新增";
            vm.consultant = {delFlag:0};
        },
        update: function () {
            var id = getSelectedRow();
            if(id == null){
                return ;
            }

            vm.showList = false;
            vm.title = "修改";
            vm.getConsultant(id);
        },
        del: function () {
            var ids = getSelectedRows();
            if(ids == null){
                return ;
            }

            confirm('确定要删除选中的记录？', function(){
                $.ajax({
                    type: "POST",
                    url: baseBusinessURL + "advisory/consultant/del",
                    contentType: "application/json",
                    data: JSON.stringify(ids),
                    success: function(r){
                        if(r.code == 0){
                            alert('操作成功', function(){
                                vm.reload();
                            });
                        }else{
                            alert(r.msg);
                        }
                    }
                });
            });
        },
        saveOrUpdate: function () {
            var url = vm.consultant.id == null ? "advisory/consultant/save" : "advisory/consultant/update";
            $.ajax({
                type: "POST",
                url: baseBusinessURL + url,
                contentType: "application/json",
                data: JSON.stringify(vm.consultant),
                success: function(r){
                    if(r.code === 0){
                        alert('操作成功', function(){
                            vm.reload();
                        });
                    }else{
                        alert(r.msg);
                    }
                }
            });
        },
        getConsultant: function(id){
            $.get(baseBusinessURL + "advisory/consultant/info/" + id, function(r){
                vm.consultant = r.consultant;
            });
        },
        reload: function () {
            vm.showList = true;
            var page = $("#jqGrid").jqGrid('getGridParam','page');
            $("#jqGrid").jqGrid('setGridParam',{
                postData:{'consultantName': vm.q.consultantName},
                page:page
            }).trigger("reloadGrid");
        }
    }
});