

Ext.apply(Ext,{
	isIE11:(function(){
	var uaAutoScroll = navigator.userAgent.toLowerCase();
	var isAutoScroll = false;
	if ( uaAutoScroll.indexOf("rv:11") > -1) {
	    isAutoScroll = true;
	}
	return isAutoScroll;
	})()
});


var chlidHeight=500,
	chlidWidth=900;
	
Ext.tree.Panel.addMembers({
    selectPathById: function(id){
        var me = this,
            node = me.getStore().getNodeById(id);
        if(node){
            me.selectPath(node.getPath());
        }
    }
});

Ext.define('WdatePickerTime',{
	extend:'Ext.form.TextField',
	itemCls:'required-field',
    getRawValue: function() {
		v = this.callParent();
		v=v.replace('年','-');
		v=v.replace('月','-');
		v=v.replace('日','');
		return v;

    },    
	listeners : {
		render : function(p) {
			Ext.get(p.getInputId()).on('click', function() {
				WdatePicker({
					dateFmt : 'yyyy年MM月dd日 HH:mm:ss',
					realFullFmt : 'yyyy-MM-dd HH:mm:ss',
					readOnly : true
					//,vel : 'startTime'
				});
			});
		}
	}
});

Ext.define('Syu.Combox',{
	extend:'Ext.form.ComboBox',
	xtype:'syuCombo',
	initComponent:function(){
		this.callParent(arguments);
		this.tpl=Ext.create('Ext.XTemplate',
          '<tpl for=".">',
          	'<div class="x-boundlist-item" >',
          	'{[typeof values === "string" ? values : values["'+this.displayField+'"]?values["'+this.displayField+'"]:"&nbsp"   ]}',
          	'</div>',
          '</tpl>');
		var mc=this.store.getProxy().getModel(),
		 	mod=new mc(),
		 	displayField=this.displayField,
		 	valueField=this.valueField;
		mod.set(displayField,'');
		mod.set(valueField,null);
		this.store.on('load',Ext.Function.pass(function(store,combo){
	      	store.insert(0,mod);
	    },[this.store,this]));
		if(this.multiSelect){
			this.on('select',function(combo,record){
				console.log(this.up('form'));
				var form=this.up('form');
				var input=form.down('hiddenfield[name="columnIndex"]');
				console.log(input);
      			if(record.length>1){
      				for(var i=0;i<record.length;i++){
      					if(!record[i].data[valueField]){
      						combo.setValue(null);
      					}
      				}
      			}
      		});			
		}
//		this.on('select',function(combo,record){
//			var form=this.up('form'),
//			    input=form.down('hiddenfield[name="columnIndex"]');
//			from.remove(input);
//			from.add({xtype:'hiddenfield',name:'columnIndex',value:col})
//  	});			
		
	}
    ,listeners:{
    	beforequery:function(e){
    		var combo = e.combo;  
              if(!e.forceAll){  
                  var input = e.query;  
                  // 检索的正则
                  var regExp = new RegExp('.*'+input+'.*','i');
                  // 执行检索
                  combo.store.filterBy(function(record,id){  
                      var text = record.get(combo.displayField);  
                      return regExp.test(text);
                  });
                  combo.expand();  
                  return false;
              }		    	
    	}
    }
});

function rendererOncClick(queryData){
	var m = Ext.MessageBox.wait("查询正在进行中...", "查询");
	Ext.Ajax.request({
		url : './../report/submit.op',
		method:'post',
		params:queryData,
		success :Ext.Function.bind(function(r,action) {
				var result = Ext.JSON.decode(r.responseText);
				m.hide();
				createGrid(result,this,true);
			 
		},queryData),
		failure : function() {
			Ext.Msg.alert("系统提示：","网络延迟超时，请刷新页面重试/");
		}
	});
}

var treeStore = Ext.create('Ext.data.TreeStore', {
                // 根节点的参数是parentId
                nodeParam : 'parentId',
                // 根节点的参数值是0
                defaultRootId : 0,
                // 属性域
                fields : ['id','text',{name:'inputInfo',type:'object'}],
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

function inititems(o,reportId,reportName,sqlName,columnindex){
   	var items=[{columnWidth:.33,type:'form',padding:'10 0 0 5',items:[]},
   			   {columnWidth:.33,type:'form',padding:'10 0 0 5',items:[]},
   			   {columnWidth:.33,type:'form',padding:'10 0 0 5',items:[]}];
   		items.push({xtype:'hiddenfield',name:'reportId',value:reportId});
   		items.push({xtype:'hiddenfield',name:'reportName',value:reportName});
   		items.push({xtype:'hiddenfield',name:'sqlName',value:sqlName});
   		items.push({xtype:'hiddenfield',name:'columnIndex',value:columnindex});
   		if(!o) return items;
	   for(var i=0;i<o.length;i++){
	   	   if(o[i].selectcolumnindex){
	   	   		
	   	   }
		   switch(i%3){
		   		case 0:
		   			items[0].items.push(createInput(o[i],reportId));
		   			break;
		   		case 1:
		   			items[1].items.push(createInput(o[i],reportId));
		   			break;
		   		case 2:
		   			items[2].items.push(createInput(o[i],reportId));
		   			break;
		   }
	   }
	   return items;
}

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
		listeners: {
		    'select': function(node, record,item) {	
		    	if(record.isLeaf()){	
//		    		if(Ext.fly(record.raw.id+'-report') && !Ext.isIE){
//		    			mainPanle.setActiveTab(record.raw.id+'-report');
//		    			return;
//		    		}
		    		mainPanle.removeAll();
					var height=30,len;
					if(record.raw.inputInfo){
						len=record.raw.inputInfo.length;
						if(len>0 && len<=3)
							height=100;
						if(len>3 && len<=6)
							height=110;
						if(len>6 && len<=8)
							height=140;
					}
		    		var	items=inititems(record.raw.inputInfo,record.raw.id,record.raw.reportname,record.raw.sqlname,record.raw.columnindex);
		    		var form=Ext.create('Syu.form',{
		    			height:height,
		    			items:items
		    		});
		    		form.setReportId(record.raw.id);
		    		var panel=Ext.create('Ext.Panel',{
		    			title:record.raw.text,
		    			id:record.raw.id+'-report',
		    			closable:true,
						layout: {
					        type: 'vbox'
					        ,align: 'stretch'
//					        ,splitterResize: true
					    },
						items:[form,
							{
								xtype:'panel',
								//region:'center',
								flex:4,
								layout:'fit',
								id:'grid-'+record.raw.id
								
							}
						]
					});
					mainPanle.add(panel);
					mainPanle.setActiveTab(panel);
		    	}
		    }   
		}
		,tbar:[{ xtype: 'button', text: '上传文件'//,hidden:true
			,handler:function(){
				var win=Ext.create('Ext.window.Window', {
				    title: '文件上传',
				    height: 120,
				    width: 400,
				    layout: 'border',
				    items:[
				    {	
				    	xtype:'form',
				    	region:'center',				    	
				    	items:[
				    		{
						        xtype: 'filefield',
						        padding:'10 5 5 10',
						        name: 'file',
						        fieldLabel: '文件',
						        labelWidth: 50,
						        msgTarget: 'side',
						        allowBlank: false,
						        anchor: '100%',
						        buttonText: '选择文件..'				    		
				    		}
				    	],
					    buttons: [{
					        text: 'Upload',
					        handler: function() {
					            var form = this.up('form').getForm();
					            if(form.isValid()){
					                form.submit({
					                    url: './../report/fileUpload.op',
					                    waitMsg: 'Uploading your photo...',
					                    success: function(fp, o) {
					                    	win.close();
					                        Ext.Msg.alert('!','文件:'+o.result.fileName+'上传成功');
					                    },
										failure: function(form, action) {
					                        console.log(action);
					                    }
					                });
					            }
					        }
					    }]					    	
				    }
				    ]
			    
				});
				win.show();
			}
		}]
});


Ext.define('Syu.SqlStatus',{
	 mixins: {
        observable: 'Ext.util.Observable'
    },
	config:{
		sqlCache:{}
	},
    constructor: function (config) {
        this.mixins.observable.constructor.call(this, config);
        this.addEvents(
            'sqlChange'
        );
    },
    setSql:function(comboId,column,value){
    	var cache=this.getSqlCache(),
    		sql=cache[comboId];  

    	if(null==value || value==''){
    		sql=sql+' and 1=2';
    		this.fireEvent('sqlChange',sql,Ext.getCmp(comboId).getStore());
    		return;
    	}
    	value=value+'';
    	value=value.split(',');

    	var exp=new RegExp("\\sand\\s"+column+"[=|\\sin\\(]\\S+\\)?");
    	//  /\sand\sprovinceId[=|\sin\(]\S+\)?/


    	if(value.length==1){  
    		if(exp.test(sql)){
    			sql=sql.replace(exp,' and '+column+'=\''+value+"'");

    		}else{
    			sql=sql+' and '+column+'=\''+value+"'";
    		}
    	}else if(!value){
    		sql=sql.replace(exp,'');
    	}else{
    		var a=[];

    		for(var i=0;i<value.length;i++){   		
    			a.push('\''+value[i]+'\'');
    			
    		}
    		if(exp.test(sql)){
    			sql=sql.replace(exp,' and '+column+' in('+a+')');
    		}else{
    			sql=sql+' and '+column+' in('+a+')';
    		}   		
    	}
    	cache[comboId]=sql;
    	this.setSqlCache(cache);
    	this.fireEvent('sqlChange',sql,Ext.getCmp(comboId).getStore());
    }
});

var createInput=(function(){
	var sqlStatatus=Ext.create('Syu.SqlStatus',{
		listeners:{
			sqlChange:function(sql,store){
				store.getProxy().setExtraParam('sql',sql);
				store.load();
			}
		}
	});
	return function(inputInfo,reportId){
		if(inputInfo.multiselect=='true' && inputInfo.multiselect){
			inputInfo.multiselect=true;
		}else{
			inputInfo.multiselect=false;
		}
		if(inputInfo.allowblank=='true' && inputInfo.allowblank){
			inputInfo.allowblank=true;
		}else{
			inputInfo.allowblank=false;
		}
		if(inputInfo.type=='text'){
			return Ext.create('Ext.form.TextField',{
				fieldLabel: inputInfo.lable,
				id:inputInfo.extid,
			    name:inputInfo.name,
			    allowBlank:inputInfo.allowblank,
			    regex:inputInfo.validateexp?new RegExp(inputInfo.validateexp):'',
			    regexText:inputInfo.validatemsg
			    
			});
		}
		if(inputInfo.type=='time'){
			return Ext.create('Ext.form.Time', {
			    name: inputInfo.name,
			    id:inputInfo.extid,
		        fieldLabel: inputInfo.lable,
		        increment: 30,
		        anchor: '100%',
		        allowBlank:inputInfo.allowblank
			});
		}
		if(inputInfo.type=='date'){
			return Ext.create('WdatePickerTime', {
			    name: inputInfo.name,
			    id:inputInfo.extid,
		        fieldLabel: inputInfo.lable,
		        anchor: '100%',
		        allowBlank:inputInfo.allowblank
			});
		}
		if(inputInfo.type=='number'){
			return Ext.create('Ext.form.Number',{
				fieldLabel: inputInfo.lable,
				id:inputInfo.extid,
			    name:inputInfo.name,
			    allowBlank:inputInfo.allowblank,
			    regex:inputInfo.validateexp?new RegExp(inputInfo.validateexp):'',
			    regexText:inputInfo.validatemsg			
			});			
		}
		if(inputInfo.type=='combo'){
			if(inputInfo.dependinput){
				if(!/where/.test(inputInfo.datasql)){
					inputInfo.datasql+=' where 1=1';
				}
				var sqlCache=sqlStatatus.getSqlCache();
				sqlCache[inputInfo.extid]=inputInfo.datasql;
				sqlStatatus.setSqlCache(sqlCache);
				if(!Ext.isArray(inputInfo.dependinput)){
					inputInfo.dependinput=Array.prototype.concat.call(Array.prototype,inputInfo.dependinput);
					inputInfo.dependcolumn=Array.prototype.concat.call(Array.prototype,inputInfo.dependcolumn);
				}
				for(var i=0;i<inputInfo.dependinput.length;i++){
					var dependCombo=Ext.getCmp(inputInfo.dependinput[i]+reportId);
					if(dependCombo){
						var fn=function(_this,column,comboId){
							var combo=Ext.getCmp(comboId);
							combo.enable();
							combo.setValue(null);
							sqlStatatus.setSql(comboId,column,_this.getValue());
							//combo.getStore().load();
						};
						dependCombo.on('select',Ext.Function.pass(fn
							,[dependCombo,inputInfo.dependcolumn[i],inputInfo.extid]));
						dependCombo.getStore().on('load',Ext.Function.pass(fn
							,[dependCombo,inputInfo.dependcolumn[i],inputInfo.extid]));
					}else{
						throw new Error('选项'+inputInfo.name+'依赖的组件'+inputInfo.dependinput[i]+'不存在');  
					}
				}
			}
			Ext.define('Syu.'+inputInfo.name+'.Store',{
				extend:'Ext.data.Store',
				config:{
					sql:inputInfo.datasql
				},
				constructor:function(){
					this.callParent(arguments);
				}
			});
			Ext.define('Syu.Model', {
			    extend: 'Ext.data.Model',
			    fields: ['VALUE','TEXT']
			});
			var store=Ext.create('Syu.'+inputInfo.name+'.Store',{
				storeId:inputInfo.extid+'-store',
				fields:['VALUE','TEXT'],
				proxy: {
			        type: 'ajax',
			        url:'./../report/getInput.op',
			        extraParams:{sql:inputInfo.datasql},
			        reader: {
			            type: 'json',
			            root: 'result'
			        },
			        scope:this
			    },
			    autoLoad:!inputInfo.allowBlank && !inputInfo.dependinput
			});

			var combo=Ext.create('Syu.Combox', {
			    fieldLabel: inputInfo.lable,
			    store: store,
			    id:inputInfo.extid,
			    name:inputInfo.name,
			    displayField: 'TEXT',
			    valueField: 'VALUE',
			    allowBlank:inputInfo.allowblank,
			    disabled:!!inputInfo.disablemsg,
			    emptyText:inputInfo.multiselect?null:inputInfo.disablemsg,
			    triggerAction:'all',
			    //editable:false,
			    multiSelect :inputInfo.multiselect
			});
			return combo;
		}	
	};
})();

Ext.define('Syu.form',{
	extend:'Ext.form.Panel',
	//region:'notrh',
	autoScroll: true,
	spilt:true,
	//height:100,
	url: './../report/submit.op',
	layout:'column',
    defaults: {
        border: 0
    },
	config:{
		reportId:null
	},
    buttons: [{
        text: '重置',
        handler: function() {
            this.up('form').getForm().reset();
        }
    }, {
        text: '查询',
        formBind: true, //only enabled once the form is valid
        disabled: true,
        handler: function() {
            var form = this.up('form').getForm();
            if (form.isValid()) {
                form.submit({
                    success: function(form, action) {
                       Ext.Msg.alert('Success', 'sdsds');
                    },
                    failure: function(form, action) {
                    	var result=action.result,
                    		queryData=form.getValues();
                    	Ext.apply(queryData,{queryLevel:1});
                    	createGrid(result,queryData);                      
                    },
                    callback:function(form, action){
                    	console.log(action);
                    }
                });
            }
        }
    }]
});




var mainPanle=Ext.create('Ext.tab.Panel',{
	region:'center',
	layout: 'fit'
});

function createGrid(result,queryData,isSub){

	if(!window['Syu.model-'+result.reportId+'-'+result.reportName+'-'+result.sqlName]){
		Ext.define('Syu.model-'+result.reportId+'-'+result.reportName+'-'+result.sqlName,{
	    	extend:'Ext.data.Model',                       	
			fields:(function(){
				var arr=[];
				for(var i=0;i<result.dataField.length;i++){
					arr.push({name:result.dataField[i],type:result.dataFieldType[i]});
				}
				return arr;
			})()
	    	
	    });
	}
    var store=Ext.create('Ext.data.Store',{
    	model:'Syu.model-'+result.reportId+'-'+result.reportName+'-'+result.sqlName,
    	proxy : {
            type : 'ajax',
            url : './../report/getData.op',
            extraParams:queryData,
            reader: {
	            type: 'json',
	            root: 'data',
	            totalProperty:'total'
	        }
        }
    });
    store.load();
	var gridWidth=0;
    for(var i=0;i<result.columns.length;i++){
    	gridWidth+=result.columns[i].width;
    	if(result.columns[i].renderer){
    		eval('var o='+result.columns[i].renderer);   		
			var q={};
    		Ext.apply(q,o,queryData);
    		result.columns[i].renderObj=q;
    		result.columns[i].renderer=function(value,cellmeta,
    				record,rowIndex,columnIndex,stroe){
    			return  Ext.String.format(
    				'<b><a href="javascript:void(0);" >'+value+'</a></b>'
    				);
    		};
    		
    	}
    }
    
    var grid=Ext.create('Ext.grid.Panel',{
    	forceFit: mainPanle.getWidth()>gridWidth?true:false,
    	store:store,
    	autoScroll:true,
    	columns:result.columns,
    	height:'auto',
    	dockedItems: [{
	        xtype: 'pagingtoolbar',
	        store: store,   // same store GridPanel is using
	        dock: 'bottom',
	        displayInfo: true
	    }],
	    tbar : new Ext.Toolbar({
			items : [
			'-', 
				{
				text : '导出Excel',
				iconCls : 'excel',
				//hidden:true,
				handler : function(){
					var fileName=result.caption,
						handName=(function(){
							var arr=[];
							for(var i=0;i<result.columns.length;i++){
								arr.push(result.columns[i].header);
							}
							return arr.join(',');
						})(),
						newD={fileName:fileName,handName:handName,isIE:(Ext.isIE || Ext.isIE10 || Ext.isIE11)?"true":"false"};
					Ext.apply(newD,queryData);
					var queryStr='./../report/excel.op?';
					for(p in newD){
						if(newD[p]){
							queryStr+=p+'='+newD[p]+'&';
						}
					}
					queryStr=queryStr.substring(0,queryStr.length-1);
					window.location.href=queryStr;
				},
				scope : this
			}, '-'
				,{
				text : '柱状图',
				iconCls : 'bar',
				hidden:(function(){
					return result.seriesDataIndex.length>0?false:true;
				})(),
				handler : Ext.Function.pass(function(store,result){
					result.data=[];
					for(var i=0;i<store.getCount();i++){
						result.data.push(store.getAt(i).data);
					}
			    	var panel=Ext.create('Ext.Panel', {
								    title: '线状图',
								    layout: 'fit',	
								    closable:true,
								    html:"<div id='barchart"+result.reportId+"' style='height:600px;width:1200px;border:0px solid white;'></div>"
								}); 
					mainPanle.add(panel);
					mainPanle.setActiveTab(panel);						
					barChart(result);
				},[store,result]),
				scope : this
			}, '-', {
				text : '线状图',
				iconCls : 'line',
				hidden:(function(){
					return result.seriesDataIndex.length>0?false:true;
				})(),				
				handler : Ext.Function.pass(function(store,result){
					result.data=[];
					for(var i=0;i<store.getCount();i++){
						result.data.push(store.getAt(i).data);
					}
			    	var panel=Ext.create('Ext.Panel', {
								    title: '线状图',
								    layout: 'fit',	
								    closable:true,
								    html:"<div id='linechart"+result.reportId+"' style='height:600px;width:1200px;border:0px solid white;'></div>"
								}); 
					mainPanle.add(panel);
					mainPanle.setActiveTab(panel);					
					lineChart(result);
				},[store,result]),
				scope : this
			}, '-', {
				text : '饼状图',
				iconCls : 'pie',
				hidden:(function(){
					return result.seriesDataIndex.length>0?false:true;
				})(),				
				handler : Ext.Function.pass(function(store,result){
					result.data=[];
					for(var i=0;i<store.getCount();i++){
						result.data.push(store.getAt(i).data);
					}				
			    	var panel=Ext.create('Ext.Panel', {
								    title: '饼状图',
								    layout: 'fit',	
								    closable:true,
								    html:"<div id='piechart"+result.reportId+"' style='height:600px;width:1200px;border:0px solid white;'></div>"
								}); 
					mainPanle.add(panel);
					mainPanle.setActiveTab(panel);
					pieChart(result);
				},[store,result]),
				scope : this
			}
			]
		})
    });
    
    grid.on('cellclick',Ext.Function.bind(function(_this, td, cellIndex, record, tr, rowIndex, e, eOpts){
    	//console.log(this[cellIndex].renderObj);
    	var queryData={};
    	console.log(this[cellIndex].renderObj);
    	Ext.apply(queryData,this[cellIndex].renderObj );
    	if(this[cellIndex].renderObj){
    		var str=this[cellIndex].dataIndex,
    			o={};
    		o[str.toLowerCase()]=record.get(str);

    		Ext.apply(queryData,o);
    		for(p in queryData){
    			if(Ext.isFunction(queryData[p])){
    				queryData[p]=queryData[p](record);
    			}
    		}
    		rendererOncClick(queryData);
    	}
    },result.columns));

    if(!isSub){
    	var p=Ext.getCmp('grid-'+result.reportId);
    	p.removeAll();
    	p.add(grid);
    }else{
    	var panel=Ext.create('Ext.Panel', {
					    title: result.caption+(queryData.title?'-'+queryData.title:''),
					    layout: 'fit',	
					    closable:true,
					    items:[{
					    	xtype:'panel',
					    	layout:'fit',
					    	items:[grid]
					    }]
					}); 
		mainPanle.add(panel);
		mainPanle.setActiveTab(panel);
    }
}



var view;

Ext.onReady(function(){
	Ext.getDoc().on("contextmenu", function(e){
	    e.stopEvent();
	});	
	view=new Ext.Viewport({ 
	   	layout:'border',
	    items: [tree,mainPanle]  
	}); 
	view.on('focus',function(){
		if(Ext.get('selectedId').getValue() && Ext.get('selectedId').getValue()!='null'){
			tree.selectPathById(Ext.get('selectedId').getValue());
			tree.hide();
		}
	});
	

});

Ext.onDocumentReady(function(){
	view.fireEvent('focus');
});