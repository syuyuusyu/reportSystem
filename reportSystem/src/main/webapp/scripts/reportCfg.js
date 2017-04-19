

var treeStore = Ext.create('Ext.data.TreeStore', {
                // 根节点的参数是parentId
                nodeParam : 'parentId',
                // 根节点的参数值是0
                defaultRootId : 0,
                // 属性域
                fields : ['id','text',{name:'inputinfo',type:'object'}],
                // 数据代理
                proxy : {
                    // 请求方式
                    type : 'ajax',
                    // 请求网址
                    url : './../report/loadTree.op'
                },
                root:{
                	text:'报表'
                }
            });
treeStore.load(); 

var tree=Ext.create('Ext.tree.Panel', {
		title: '报表目录',
		region:'west',
		store: treeStore,
		width:200,
		useArrows: true,
		frame: true,
		autoScroll: true,
		collapsible: true,
		containerScroll: true,
		split: true, 
		tbar:[{ xtype: 'button', text: '添加修改查询条件',
			handler:function(){
				if(!mainPanle.contains(inputCfgPanel)){
					mainPanle.add(inputCfgPanel);
				}
				mainPanle.setActiveTab(inputCfgPanel);
				queryGrid.getStore().load();
			}
		}],
		listeners: {
		    'itemcontextmenu': function(_this, record, item, index, e, eOpts) {
				//e.preventDefault();menu.showAt(e.getPoint());
		    	if(!record.isLeaf()){
		    		Ext.create('Ext.menu.Menu',{
		    			plain: true,
		    			margin: '0 0 10 0',
		    			items:[
		    				{text:'新建目录',iconCls:'report-edit',handler:Ext.Function.pass(this.addDirectory,[record.raw.id])},
		    				{text:'新建报表',iconCls: 'report-add',handler:this.addReport}		    				
		    			]
		    		}).showAt(e.getPoint());

		    	}
		    }   
		}
		,addDirectory:function(id){
			Ext.Msg.prompt('!!','请输入目录名称',function(a,text){
				if(a=='ok'){
					Ext.Ajax.request({
						url:'./../cfg/addTree.op',
						method:'post',
						params:{parentId:id,title:text},
						success :function(r,action) {
								treeStore.load();
								Ext.Msg.alert("系统提示:",'添加目录成功');
							 
						},
						failure : function() {
							Ext.Msg.alert("系统提示：","网络延迟超时，请刷新页面重试/");
						},
						scope:this
					});
				}
			});

		}
		,addReport:function(){
			
		}
});

var mainPanle=Ext.create('Ext.tab.Panel',{
	region:'center',
	layout: 'fit'
});
//ID, NAME, TYPE, LABLE, MULTISELECT, VALIDATEEXP, VALIDATEMSG, ALLOWBLANK, DEPENDCOLUMN, DEPENDINPUT, DISABLEMSG, DATASQL
Ext.define('Syu.QueryGridModel',{
	extend:'Ext.data.Model',
	fields:['ID','NAME','TYPE','LABLE','MULTISELECT','VALIDATEEXP','VALIDATEMSG','ALLOWBLANK','DEPENDCOLUMN','DEPENDINPUT','DISABLEMSG','DATASQL','DESCRIPTION']
	
});
var queryGridStore=Ext.create('Ext.data.Store',{
			model:'Syu.QueryGridModel',
			proxy : {
	            type : 'ajax',
	            url : './../cfg/showInput.op',
	            //extraParams:queryData,
	            reader: {
		            type: 'json',
		            root: 'data',
		            totalProperty:'total'
		        }
	        }
		});
var inputForm=Ext.create('Ext.form.Panel',{
	url:'./../cfg/updateInput.op',
	spilt:true,
	height:'auto',
	items:[{layout:'column',items:[
		{columnWidth:.33,type:'form',border:0,padding:'10 0 0 5',
		items:[
			{xtype:'textfield',disabled:true,fieldLabel:'id',id:'inputId',name:'ID'},
			{xtype:'combo',fieldLabel:'类型',id:'inputType',name:'TYPE',valueField:'value',allowBlank:false,
				store:Ext.create('Ext.data.Store',{
					fields:['value','text'],
					data:[{value:'combo',text:'下拉框'},{value:'text',text:'文字输入'},
						{value:'number',text:'数字输入'},{value:'date',text:'日期输入'}]
				}),
				listeners:{
					change:function(combo,newV,opt){
						if(newV=='combo'){
							Ext.Array.each(this.up('form').query('textfield[flag=text]'),function(com){
								com.disable();
							});
							Ext.Array.each(this.up('form').query('textfield[flag=combo]'),function(com){
								com.enable();
							});							
						}else{
							Ext.Array.each(this.up('form').query('textfield[flag=text]'),function(com){
								com.enable();
							});
							Ext.Array.each(this.up('form').query('textfield[flag=combo]'),function(com){
								com.disable();
							});						
						}						
					}
				}
			},
			{xtype:'textfield',fieldLabel:'验证正则表达式',id:'inputRegExp',name:'VALIDATEEXP',flag:'text'},
			{xtype:'textfield',fieldLabel:'依赖SQL列',id:'inputDependSql',name:'DEPENDCOLUMN',flag:'combo'}
		]},
		{columnWidth:.33,type:'form',border:0,padding:'10 0 0 5',
		items:[
			{xtype:'textfield',fieldLabel:'描述',id:'inputDesc',name:'DESCRIPTION',allowBlank:false},
			{xtype:'textfield',fieldLabel:'标签名称',id:'inputName',name:'LABLE',allowBlank:false},
			{xtype:'textfield',fieldLabel:'验证说明',id:'inputRegDesc',name:'VALIDATEMSG',flag:'text'},
			{xtype:'combo',fieldLabel:'依赖条件参数名称',id:'inputDependName',name:'DEPENDINPUT',flag:'combo'}
		]},
		{columnWidth:.33,type:'form',border:0,padding:'10 0 0 5',
		items:[
			{xtype:'textfield',fieldLabel:'参数名称',id:'inputParmaName',name:'NAME',allowBlank:false},
			{xtype:'combo',fieldLabel:'是否多选',id:'inputIsMulti',name:'MULTISELECT',valueField:'value',flag:'combo',allowBlank:false,
				store:Ext.create('Ext.data.Store',{
					fields:['value','text'],
					data:[{value:'true',text:'是'},{value:'false',text:'否'}]
				})
			},
			{xtype:'combo',fieldLabel:'是否为空',id:'inputIsBlank',name:'ALLOWBLANK',valueField:'value',allowBlank:false,
				store:Ext.create('Ext.data.Store',{
					fields:['value','text'],
					data:[{value:'true',text:'是'},{value:'false',text:'否'}]
				})
			},
			{xtype:'textfield',fieldLabel:'不可用提示',id:'inputValidDesc',name:'DISABLEMSG'}
		]}	
	]}
		 ,{xtype:'hidden',name:'isUpdate',id:'isUpdate'}
		,{
			xtype     : 'textareafield',
	        grow      : true,
	        allowBlank:false,
	        id        : 'inputSql',
	        name      : 'DATASQL',
	        fieldLabel: 'SQL',
	        flag:'combo',
	        anchor    : '95%'
		}
	],
	
    defaults: {
        border: 0
    },
	config:{
		reportId:1
	},
    buttons: [{
        text: '重置',
        handler: function() {
        	console.log(this.up('form').getForm().getValues());
            this.up('form').getForm().reset();
        }
    }, {
        text: '提交',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
        	if(!Ext.getCmp('inputId').getValue()){
        		Ext.Msg.alert('!','ID不能为空');
        		return;
        	}
            var form = this.up('form').getForm();
            console.log(form.isValid());
            if (form.isValid()) {
                form.submit({
                	params: {
				        ID: Ext.getCmp('inputId').getValue()
				    },
                    success: function(form, action) {
                       Ext.Msg.alert('!', '修改成功');
                       queryGridStore.load();
                       form.reset();
                    },
                    failure: function(form, action) {
                    	Ext.Msg.alert('!', '修改失败');               
                    }

                });
            }
        }
    }]
});

var queryGrid=Ext.create('Ext.grid.GridPanel',{
	store:queryGridStore,
	//forceFit: true,
	autoScroll:true,
	height:390,
	columns:[
		{text:'id',dataIndex:'ID',width:30},
		{text:'描述',dataIndex:'DESCRIPTION',width:150},
		{text:'参数名称',dataIndex:'NAME',width:100},
		{text:'类型',dataIndex:'TYPE',width:70},
		{text:'标签名称',dataIndex:'LABLE',width:100},
		{text:'是否多选',dataIndex:'MULTISELECT',width:70},
		{text:'验证正则表达式',dataIndex:'VALIDATEEXP',width:200},
		{text:'验证说明',dataIndex:'VALIDATEMSG',width:200},
		{text:'是否为空',dataIndex:'ALLOWBLANK',width:70},
		{text:'依赖sql列',dataIndex:'DEPENDCOLUMN',width:70},
		{text:'依赖条件参数名称',dataIndex:'DEPENDINPUT',width:110},
		{text:'不可用提示',dataIndex:'DISABLEMSG',width:100},
		{text:'SQL',dataIndex:'DATASQL',width:400}
		
	],
    dockedItems: [{
        xtype: 'pagingtoolbar',
        store: queryGridStore,
        dock: 'bottom',
        pageSize:10,
        displayInfo: true
    	}],
    listeners:{
    	itemcontextmenu:function(grid, record, item, index, e, eOpts ){
    		Ext.create('Ext.menu.Menu',{
    			items:[
  					{text:'添加',iconCls:'report-edit'
  						,handler:function(){
  							inputForm.getForm().reset();
							Ext.getCmp('isUpdate').setValue('create');
							Ext.getCmp('inputId').setValue(grid.store.max('ID')+1);
  						}
  					},
		    		{text:'修改',iconCls: 'report-add'
		    			,handler:function(){
							inputForm.getForm().reset();
							Ext.getCmp('isUpdate').setValue('update');
							Ext.getCmp('inputId').setValue(record.raw.ID);
							Ext.getCmp('inputDesc').setValue(record.raw.DESCRIPTION);
							Ext.getCmp('inputParmaName').setValue(record.raw.NAME);
							Ext.getCmp('inputType').setValue(record.raw.TYPE);
							Ext.getCmp('inputName').setValue(record.raw.LABLE);
							Ext.getCmp('inputIsMulti').setValue(record.raw.MULTISELECT);
							Ext.getCmp('inputRegExp').setValue(record.raw.VALIDATEEXP);
							Ext.getCmp('inputRegDesc').setValue(record.raw.VALIDATEMSG);
							Ext.getCmp('inputIsBlank').setValue(record.raw.ALLOWBLANK);
							Ext.getCmp('inputDependSql').setValue(record.raw.DEPENDCOLUMN);
							Ext.getCmp('inputDependName').setValue(record.raw.DEPENDINPUT);
							Ext.getCmp('inputValidDesc').setValue(record.raw.DISABLEMSG);
							Ext.getCmp('inputSql').setValue(record.raw.DATASQL);
		    			}
		    		},	 
		    		{text:'删除',iconCls: 'report-add'
		    			,handler:function(){
		    				Ext.Msg.confirm('系统提示','确定删除该选择条件?',function(a){
		    					if(a=='yes'){
				    				Ext.Ajax.request({
				    					url:'./../report/deleteInput.op',
				    					params:{id:record.get('ID')},
				    					success:function(){
				    						queryGridStore.load();
				    					}
				    				});
		    					}
		    				});		    			
		    			}
		    		}	 
    			]
    		}).showAt(e.getPoint());
    	}
    }
});

var inputCfgPanel=Ext.create('Ext.Panel',{
	title:'添加修改查询条件',
	closable:true,
	layout: {
        type: 'vbox'
        ,align: 'stretch'					        
        //,splitterResize: true
    },
	items:[{xtype:'panel',layout:'fit',items:[queryGrid]},inputForm]
});

Ext.onReady(function(){
	Ext.getDoc().on("contextmenu", function(e){
	    e.stopEvent();
	});
	new Ext.Viewport({ 
	   	layout:'border',
	    items: [tree,mainPanle]  
	}); 
});