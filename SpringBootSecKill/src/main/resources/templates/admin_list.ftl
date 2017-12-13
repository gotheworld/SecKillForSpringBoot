<!DOCTYPE html>
<html lang="zh-CN">
<head>
<#include "common/head.ftl">
<title>秒杀列表页</title>
</head>
<body>
	<!-- 页面显示部分 -->
	<div class="container">
	
		<div class="panel panel-default">
		
			<div class="panel-heading text-center">
				<h2>后台库存信息</h2>
			</div>
			<div class="panel-body">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>名称</th>
							<th>库存</th>
							<th>开始时间</th>
							<th>结束时间</th>
							<th>创建时间</th>
							<th>操作</th>
						</tr>
					</thead>
					<tbody>
						<#list list as sk>
                              <tr>
                                      <td><input type=text value=${sk.name}></td>
                                      <td><input type=text value=${sk.number}></td>
                                      <td><input type=text value="${sk.startTime?datetime}"></td>
                                      <td><input type=text value="${sk.endTime?datetime}"></td>
                                      <td>${sk.createTime?datetime}</td>

									  <td>
										  <a class="btn btn-info" href="${basePath}/seckill/${sk.seckillId?c}/detail" target="_blank">
											   保存
										  </a>
									  </td>
                     			</tr>
                        </#list>
					</tbody>
				</table>
			</div>
		</div>
		  <form class="form-horizontal" role="form">
			  <div class="form-group">
			    <label for="firstname" class="col-sm-2 control-label">名称</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="firstname" placeholder="请输入名称">
			    </div>
			  </div>
			  <div class="form-group">
			    <label for="lastname" class="col-sm-2 control-label">库存</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="lastname" placeholder="请输入库存">
			    </div>
			  </div>
			   <div class="form-group">
			    <label for="lastname" class="col-sm-2 control-label">开始时间</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="lastname" placeholder="请输入开始时间">
			    </div>
			  </div>
			   <div class="form-group">
			    <label for="lastname" class="col-sm-2 control-label">结束时间</label>
			    <div class="col-sm-10">
			      <input type="text" class="form-control" id="lastname" placeholder="请输入结束时间">
			    </div>
			  </div>
			  <div class="form-group">
			    <div class="col-sm-offset-2 col-sm-10">
			      <button type="submit" class="btn btn-success">添加秒杀信息</button>
			    </div>
			  </div>
        </form>
	</div>

	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>