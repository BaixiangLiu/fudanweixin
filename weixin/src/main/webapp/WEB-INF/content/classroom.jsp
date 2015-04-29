<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
<title>复旦信息办--课程</title>
<link href="css/bootstrap.min.css" rel="stylesheet">
 <link href="css/bootstrap-theme.min.css" rel="stylesheet">
<script src="js/jquery-1.11.1.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<link href="css/datepicker3.css" rel="stylesheet" />
<script src="js/bootstrap-datepicker.js" ></script>
<link href="css/bootstrap-select.css" rel="stylesheet" >
<script src="js/bootstrap-select.js"  type="text/javascript" > </script>

<script type="text/javascript">
var startdate=null;
	$(function() {
		
		
		$.getJSON("resource/schoolterm.act",{},function(resp){
			if (resp.result.errcode == 0) {
				var thisweek=resp.list.week;
				$("#pcontent").html(
						"<div class='alert alert-success'>现在是"+resp.list.termname+"学期的第"+thisweek+"周</div>");
				//加载课程配置
				$.getJSON("http://map.fudan.edu.cn/src/mobile/paike/status.php?callback=?",function(data){
					var ts=$("#ts")[0];
					var te=$("#te")[0];
					for(i=0;i< data.lessons.length;i++){						
						ts.options.add(new Option("第"+(i+1)+"节("+data.lessons[i].substring(0,data.lessons[i].indexOf("-"))+")",i+1));
						te.options.add(new Option("第"+(i+1)+"节("+data.lessons[i].substring(data.lessons[i].indexOf("-")+1)+")",i+1));
					}
					$('.selectpicker').selectpicker('refresh');
					startdate=new Date(data.firstday);
					var bdate=new Date(data.firstday);
					bdate.setTime(bdate.getTime()+7*24*3600000*data.weeks);
					$("#date").datepicker({format:"yyyy-mm-dd", language: "zh-CN", orientation: "top auto",autoclose:true,todayHighlight:true, daysOfWeekDisabled: "0,6", startDate:data.firstday,endDate:bdate.toISOString().substring(0,10)});
				});				
			} else {
				$("#pcontent").html(
						"<div class='alert alert-danger'>"
								+ resp.result.errdesc
								+ "</div>");
			}			
		});
		
	});
	function getParameterByName(name) {
		var match = RegExp('[?&]' + name + '=([^&]*)').exec(
				window.location.search);
		return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
	}
	var qdate=null;
	function getWeek()
	{
		var date=$("#date").val();
		
		if(date==null || date=="")
			qdate=new Date();
		else
			qdate=new Date(date);
		
		return Math.floor((qdate.getTime()-startdate.getTime())/(3600000*24*7))+1;
	}
	function subform(){
		
		$.getJSON("http://map.fudan.edu.cn/src/mobile/paike/index.php?w="+getWeek()+"&d="+qdate.getDay()+"&b="+$("#classrm").val()+"&ts="+$("#ts").val()+"&te="+$("#te").val()+"&callback=?",function(data){
			var htm="";
			for(i=0;i<data.length;i++){
				htm += "<a href=\"javascript:qclass('"+data[i]+"')\">"+data[i]+"</a> &nbsp; ";
			}
			$("#pcontent").html(htm);
		});
			
		
		return false;		
	}
	function qclass(clr)
	{
		$.getJSON("http://map.fudan.edu.cn/src/mobile/paike/classroom.php?w="+getWeek()+"&roomid="+clr+"&callback=?",function(data){
			var html="<table class='table table-striped table-bordered '><tr><th></th><th>周一</th><th>周二</th><th>周三</th><th>周四</th><th>周五</th></tr>";
			var classcount=$("#ts")[0].options.length;
			for(i=0;i<classcount;i++){
				html+="<tr><td>第"+(i+1)+"节</td>";
				for(j=0;j<5;j++)
					html+="<td>"+data[j*classcount+i]+"</td>";
				html+="</tr>";	
			}
			html+="</table>";
			$("#lessons").html(html);
		});		
	}
</script>
</head>

<body>

	<div class="container-fluid">
			<s:include value="/common/wxnav.jsp" />

				<s:actionerror />
				<div class=" panel panel-primary">
					<div class="panel-heading">
						<form class="form-inline" role="form" onsubmit="return subform()"
							id="qform">							
							<div class="form-group">
								<div class="input-group"><span
										class="input-group-addon glyphicon glyphicon-calendar"  ></span> 
									<input type="text" id="date" class="form-control" /> <select
										class="selectpicker" id="classrm">
										<option value="H2">邯郸二教</option>
										<option value="H3">邯郸三教</option>
										<option value="H4">邯郸四教</option>
										<option value="H5">邯郸五教</option>
										<option value="H6">邯郸六教</option>
										<option value="HGX">光华楼西辅楼</option>
										<option value="HGW">光华楼西主楼</option>
										<option value="F2">枫林新教学楼</option>
										<option value="Z2">张江二教</option>
										<option value="JA">江湾教学楼A</option>
										<option value="JB">江湾教学楼B</option>
										</select>
								</div>
							</div>
						<div class="form-group">
								<div class="input-group">
									<select class="selectpicker" id="ts"></select> <span
										class="input-group-addon glyphicon glyphicon-minus"></span> <select
										class="selectpicker" id="te"></select>
								</div>
							</div>
							<button type="submit" class="btn btn-success btn-sm">空闲教室查询</button>

						</form>
					</div>
					<div class="panel-body" >
					<div class="alert" id="pcontent"></div>
					<div id="lessons"></div>
					</div>
					
				</div>
			</div>

	<div class="alert alert-warning">空闲教室仅根据本科和研究生排课课表计算而出，且仅有教学周的周一至周五的数据比较准确，同时无法排除临时调课、讲座或其它活动占用、管理员锁门等情况。
	<br />因而本信息仅供参考，请根据实际教室占用情况合理安排自修。</div>

		<s:include value="/common/foot.jsp" />




</body>
</html>
