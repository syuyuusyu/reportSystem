function lineChart(resultData){
	console.log(resultData);
	require.config({
    paths: {
        	echarts: '../resources/echart'
	    }
	});
	require(
	    [
	        'echarts',
	        'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
	        'echarts/chart/bar'
	    ],
	   
    function (ec) {
        var myChart = ec.init(document.getElementById('linechart'+resultData.reportId));
        function initData(){
        	var option = {
        		    title : {
        		        text: '',
        		        subtext: ''
        		    },
        		    tooltip : {
        		        trigger: 'axis'
        		    },
        		    legend: {
        		        data:[]
        		    },
        		    toolbox: {
        		        show : true,
        		        feature : {
        		            mark : {show: true},
        		            dataView : {show: true, readOnly: false},
        		            magicType : {show: true, type: ['line', 'bar']},
        		            restore : {show: true},
        		            saveAsImage : {show: true}
        		        }
        		    },
        		    calculable : true,
        		    xAxis : [
        		        {
        		            type : 'category',
        		            boundaryGap : false,
        		            data : [],
        		            axisLabel: {
        		        		rotate:-45
        		        	}
        		        }
        		    ],
        		    yAxis : [
        		        {
        		            type : 'value',
        		            axisLabel : {
        		                formatter: '{value} '
        		            }
        		        }
        		    ],
        		    series : []
        		};
        	option.title.text=resultData.caption;
        	option.legend.data=resultData.seriesHeader;
        	var xAxisData=[];
        	for(var j=0;j<resultData.seriesDataIndex.length;j++){
        		var seriesTemp={
        	            name:'',
        	            type:'line',
        	            data:[],
        	            markPoint : {
        	                data : [
        	                    {type : 'max', name: '最大值'},
        	                    {type : 'min', name: '最小值'}
        	                ]
        	            },
        	            markLine : {
        	                data : [
        	                    {type : 'average', name: '平均值'}
        	                ]
        	            }
        			};
        		seriesTemp.name=resultData.seriesHeader[j];
        		seriesTemp.data=[];
        		for(var i=0;i<resultData.data.length;i++){
        			if(j==0){
        				xAxisData.push(resultData.data[i][resultData.cateDataIndex]);
        			}
        			seriesTemp.data.push(resultData.data[i][resultData.seriesDataIndex[j]]);
        		}
        		option.series.push(seriesTemp);
        	}
        	option.xAxis[0].data=xAxisData;
        	return option;
        }
        var ecConfig = require('echarts/config');  
        //var zrEvent = require('zrender/tool/event');
		console.log(resultData);
        myChart.setOption(initData());
        if(resultData.seriesDataIndex.length>1){
            myChart.on(ecConfig.EVENT.CLICK,function(param){
            	if(typeof param.data !='object'){
    	        	var i=param.dataIndex;
			    	var panel=Ext.create('Ext.Panel', {
								    title: '子线图-'+resultData.data[i][resultData.cateDataIndex],
								    layout: 'fit',	
								    closable:true,
								    html:"<div id='sublinechart"+resultData.reportId+i+"' style='height:600px;width:1200px;border:0px solid white;'></div>"
								}); 
					mainPanle.add(panel);
					mainPanle.setActiveTab(panel);					
					sublinechart(resultData,i); 
            	}
            });
        }
    }
    
);
}


function barChart(resultData){
require.config({
    paths: {
        echarts: '../resources/echart'
    }
});
require(
    [
        'echarts',
        'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
        'echarts/chart/bar'
    ],
    
    function (ec) {
        var myChart = ec.init(document.getElementById('barchart'+resultData.reportId));
        function initData(){
        	var option = {
        		    title : {
        		        text: '',
        		        subtext: ''
        		    },
        		    tooltip : {
        		        trigger: 'axis'
        		    },
        		    legend: {
        		        data:[]
        		    },
        		    toolbox: {
        		        show : true,
        		        feature : {
        		            mark : {show: true},
        		            dataView : {show: true, readOnly: false},
        		            magicType : {show: true, type: ['line', 'bar']},
        		            restore : {show: true},
        		            saveAsImage : {show: true}
        		        }
        		    },
        		    calculable : true,
        		    xAxis : [
        		        {
        		            type : 'category',
        		            boundaryGap : false,
        		            data : [],
        		            axisLabel: {
        		        		rotate:-45
        		        	}
        		        }
        		    ],
        		    yAxis : [
        		        {
        		            type : 'value',
        		            axisLabel : {
        		                formatter: '{value} '
        		            }
        		        }
        		    ],
        		    series : []
        		};
        	option.title.text=resultData.caption;
        	option.legend.data=resultData.seriesHeader;
        	var xAxisData=[];
        	for(var j=0;j<resultData.seriesDataIndex.length;j++){
        		var seriesTemp={
        	            name:'',
        	            type:'bar',
						itemStyle : {
							normal : {
								label : {
									show : true,
									textStyle : {
										color : '#800080'
									}
								}
							}
						},
        	            data:[],
        	            markPoint : {
        	                data : [
        	                    {type : 'max', name: '最大值'},
        	                    {type : 'min', name: '最小值'}
        	                ]
        	            },
        	            markLine : {
        	                data : [
        	                    {type : 'average', name: '平均值'}
        	                ]
        	            }
        			};
        		seriesTemp.name=resultData.seriesHeader[j];
        		seriesTemp.data=[];
        		for(var i=0;i<resultData.data.length;i++){
        			if(j==0){
        				xAxisData.push(resultData.data[i][resultData.cateDataIndex]);
        			}
        			seriesTemp.data.push(resultData.data[i][resultData.seriesDataIndex[j]]);
        		}
        		option.series.push(seriesTemp);
        	}
        	option.xAxis[0].data=xAxisData;
        	return option;
        }
        var ecConfig = require('echarts/config');  
       // var zrEvent = require('zrender/tool/event');
        

        myChart.setOption(initData());
        if(resultData.seriesDataIndex.length>1){
            myChart.on(ecConfig.EVENT.CLICK,function(param){
            	if(typeof param.data !='object'){
    	        	var i=param.dataIndex;
			    	var panel=Ext.create('Ext.Panel', {
								    title: '子柱图-'+resultData.data[i][resultData.cateDataIndex],
								    layout: 'fit',	
								    closable:true,
								    html:"<div id='subbarchart"+resultData.reportId+i+"' style='height:600px;width:1200px;border:0px solid white;'></div>"
								}); 
					mainPanle.add(panel);
					mainPanle.setActiveTab(panel);						
					subbarchart(resultData,i); 
	            	}
            });
        }
    }
    
);
}


function pieChart(resultData){
	console.log(11111);
	var width=1200;
	var height=600;
	function initData(){
	var option = {
		    title : {
		        text: '',
		        subtext: '',
		        x:'center'
		    },
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    legend: {
		        x : 'center',
		        y : 'bottom',
		        selectedMode:'single',
		        data:[],
		        selected:function(){
		        	var obj={};
		        	for(var i=1;i<resultData.seriesHeader.length;i++){
		        		obj[resultData.seriesHeader[i]]=false;
		        	}
		        	return obj;
		        }()
		    },
		    toolbox: {
		        show : true,
		        feature : {
		            mark : {show: true},
		            dataView : {show: true, readOnly: false},
		            magicType : {
		                show: true, 
		                type: ['pie']
		            },
		            restore : {show: true},
		            saveAsImage : {show: true}
		        }
		    },
		    calculable : true,
		    series : []
		};
	option.title.text=resultData.caption;
	option.legend.data=resultData.seriesHeader;

	for(var j=0;j<resultData.seriesDataIndex.length;j++){
		var seriesTemp=	        {
	            name:'',
	            type:'pie',
	            //radius : [20, 110],
	            //center : ['25%', 300],
	            //roseType : 'radius',
	            //width: '40%',       // for funnel
	            //max: 40,            // for funnel
	            itemStyle : {
	                normal : {
	                    label : {
	                        show : true,
	                        formatter: '{b} : {c} ({d}%)' 
	                    },
	                    labelLine : {
	                        show : true
	                    }
	                },
	                emphasis : {
	                    label : {
	                        show : true
	                    },
	                    labelLine : {
	                        show : true
	                    }
	                }
	            },
	            data:[]
	        };

		seriesTemp.radius=[20,160];
		seriesTemp.center=[width/2,height/2];
		seriesTemp.name=resultData.seriesHeader[j];
		seriesTemp.data=[];
		for(var i=0;i<resultData.data.length;i++){
			seriesTemp.data.push({name:resultData.data[i][resultData.cateDataIndex],value:resultData.data[i][resultData.seriesDataIndex[j]]});
		}
		option.series.push(seriesTemp);
	}
	return option;
}


require.config({
    paths: {
        echarts: '../resources/echart'
    }
});
require(
    [
        'echarts',
        'echarts/chart/pie'
    ],
    
    function (ec) {
        var myChart = ec.init(document.getElementById('piechart'+resultData.reportId));
        //console.log(initData());
        myChart.setOption(initData());
        var ecConfig = require('echarts/config');  
        if(resultData.seriesDataIndex.length>1){
            myChart.on(ecConfig.EVENT.CLICK,function(param){
	        	var i=param.dataIndex;
		    	var panel=Ext.create('Ext.Panel', {
							    title: '子饼图-'+resultData.data[i][resultData.cateDataIndex],
							    layout: 'fit',	
							    closable:true,
							    html:"<div id='subpiechart"+resultData.reportId+i+"' style='height:600px;width:1200px;border:0px solid white;'></div>"
							}); 
				mainPanle.add(panel);
				mainPanle.setActiveTab(panel);				
				subpiechart(resultData,i);           	
            });
        }
    }
);
}

function subbarchart(resultData,i){
subOption= {
	    title : {
	        text: resultData.data[i][resultData.cateDataIndex]
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:[]
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data:(function(){
	            	var data=[];
	            	for(var j=0;j<resultData.seriesHeader.length;j++){
	            		if(resultData.seriesDataIndex[j].toLowerCase()!=="count")
	            		data.push(resultData.seriesHeader[j]);
	            	}
	            	return data;
	            })(),
	            axisLabel: {
	        		rotate:-45
	        	}
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            axisLabel : {
	                formatter: '{value} '
	            }
	        }
	    ],
	    series : [{
	    	name:'',
	    	type:'bar',
			itemStyle : {
				normal : {
					label : {
						show : true,
						textStyle : {
							color : '#800080'
						}
					}
				}
			},
	    	data:(function(){
	    		var a=[];
	    		for(var j=0;j<resultData.seriesDataIndex.length;j++){	    			
	    			if(resultData.seriesDataIndex[j].toLowerCase()!=="count"){
	    				a.push(resultData.data[i][resultData.seriesDataIndex[j]]);
	    			}
	    		}
	    		return a;
	    	})()
	    }]
	};          
require.config({
    paths: {
        echarts: '../resources/echart'
    }
});
require(
    [
        'echarts',
        'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
        'echarts/chart/bar'
    ],
    
    function (ec) {
        var myChart = ec.init(document.getElementById('subbarchart'+resultData.reportId+i));
        //console.log(eval(window.opener.subOption));
        myChart.setOption(subOption);

    }
    
);
}

function subpiechart(resultData,i){
var option = {
	    title : {
	        text: resultData.data[i][resultData.cateDataIndex],
	        subtext: '',
	        x:'center'
	    },
	    tooltip : {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	    },
	    legend: {
	        x : 'center',
	        y : 'bottom',
	        data:[]
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {
	                show: true, 
	                type: ['pie']
	            },
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    series : [{
            name:'',
            type:'pie',
            radius : [20, 180],
            center : ['50%', 300],
            itemStyle : {
                normal : {
                    label : {
                        show : true,
                        formatter: '{b} : {c} ({d}%)' 
                    },
                    labelLine : {
                        show : true
                    }
                },
                emphasis : {
                    label : {
                        show : true
                    },
                    labelLine : {
                        show : true
                    }
                }
            },
            data:(function(){
	    		var a=[];
	    		for(var j=0;j<resultData.seriesDataIndex.length;j++){
	    			if(resultData.seriesDataIndex[j].toLowerCase()!=="count"){
	    				a.push(
		    					{value:resultData.data[i][resultData.seriesDataIndex[j]],
		    					name:resultData.seriesHeader[j]}
		    				);
	    			}
	    			
	    		}
	 
	    		return a;
            })()
        }]
	};

require.config({
    paths: {
        echarts: '../resources/echart'
    }
});
require(
    [
        'echarts',
        'echarts/chart/pie'
    ],
    
    function (ec) {
        var myChart = ec.init(document.getElementById('subpiechart'+resultData.reportId+i));
        //console.log(eval(window.opener.subOption));
        myChart.setOption(option);

    });
    
}


function sublinechart(resultData,i){
subOption= {
	    title : {
	        text: resultData.data[i][resultData.cateDataIndex]
	    },
	    tooltip : {
	        trigger: 'axis'
	    },
	    legend: {
	        data:[]
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            mark : {show: true},
	            dataView : {show: true, readOnly: false},
	            magicType : {show: true, type: ['line', 'bar']},
	            restore : {show: true},
	            saveAsImage : {show: true}
	        }
	    },
	    calculable : true,
	    xAxis : [
	        {
	            type : 'category',
	            boundaryGap : false,
	            data:(function(){
	            	var data=[];
	            	for(var j=0;j<resultData.seriesHeader.length;j++){
	            		if(resultData.seriesDataIndex[j].toLowerCase()!=="count")
	            		data.push(resultData.seriesHeader[j]);
	            	}
	            	return data;
	            })(),
	            axisLabel: {
	        		rotate:-45
	        	}
	        }
	    ],
	    yAxis : [
	        {
	            type : 'value',
	            axisLabel : {
	                formatter: '{value} '
	            }
	        }
	    ],
	    series : [{
	    	name:'',
	    	type:'line',
			itemStyle : {
				normal : {
					label : {
						show : true,
						textStyle : {
							color : '#800080'
						}
					}
				}
			},
	    	data:(function(){
	    		var a=[];
	    		for(var j=0;j<resultData.seriesDataIndex.length;j++){
	    			if(resultData.seriesDataIndex[j].toLowerCase()!=="count"){
	    				a.push(resultData.data[i][resultData.seriesDataIndex[j]]);
	    			}
	    			
	    		}
	    		return a;
	    	})()
	    }]
	};          
require.config({
    paths: {
        echarts: '../resources/echart'
    }
});
require(
    [
        'echarts',
        'echarts/chart/line',   // 按需加载所需图表，如需动态类型切换功能，别忘了同时加载相应图表
        'echarts/chart/bar'
    ],
    
    function (ec) {
        var myChart = ec.init(document.getElementById('sublinechart'+resultData.reportId+i));
        //console.log(eval(window.opener.subOption));
        myChart.setOption(subOption);

    }
    
);
}