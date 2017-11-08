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
				<h2>秒杀列表</h2>
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
							<th>详情页</th>
						</tr>
					</thead>
					<tbody>
						<#list list as sk>
                              <tr>
                                      <td>${sk.name}</td>
                                      <td>${sk.number}</td>
                                      <td>${sk.startTime?datetime}</td>
                                      <td>${sk.endTime?datetime}</td>
                                      <td>${sk.createTime?datetime}</td>

									  <td>
										  <a class="btn btn-info" href="http://localhost:8080/seckill/${sk.seckillId?c}/detail" target="_blank">
											   详情
										  </a>
									  </td>
                     			</tr>
                        </#list>
					</tbody>
				</table>
				<a class="btn btn-success" href="http://localhost:8080/seckill/admin/list" target="_blank">
				    更新库存
				</a>
			</div>
		</div>
	</div>

	<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
	<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>
	<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
	<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</body>
</html>