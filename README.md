# futures_trading_sys

## 项目简介

基于Springboot2和Vue，目前上传的python后端基于xinYi授权交易，另有基于windowsAPI的本地交易模块（支持更多股票/期货软件）
项目前端链接：https://github.com/Lawlietqwq/futures-dealing


## 项目目录：

```lua
futurestradingsystem
├── common -- 工具类及通用代码模块，统一异常处理和响应报文
├── config-- 项目配置类
     ├── redis
     ├── security   用户认证和用户验证
     ├── CORSConfig.java 跨域配置
     ├── Jackson.java 
     ├── SpringThreadPoolConfig.java 线程池配置
├── controller  Spring后端接口
├── dao -- 数据持久层, 访问数据库
├── dto -- 数据传输对象
├── entity -- 实体类
├── intercepter-- 拦截器，防止重复提交
├── mq -- RocketMQ消息队列消费binlog同步缓存
├── patterns -- 策略模式
     ├── handler -- 缓存同步处理器
├── service -- 业务逻辑层，负责对合约、持仓、策略以及用户的一些操作
├── strategy -- 策略相关，实现期货交易所用策略
     ├── impl  包含所有交易策略类，必须实现TradingExecutable接口
     ├── AsyncTask.java 负责所有模型实例的运行，采用线程池运行每一个模型实例
     ├── ModelInstance.java 完整的交易类，主要由开仓类和平仓类组成
     ├── TradingExecutable.interface 所有策略类必须实现的接口，代表策略类必须进行的操作
├── util -- 工具类库
     ├── HttpClientUtil.java  负责主服务和下单服务使用Http进行通信
     ├── InitModel.java 在系统异常时重新加载所有实例模型
└── vo -- 视图包装对象

```