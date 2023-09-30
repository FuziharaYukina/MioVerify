# MioVerify

![Static Badge](https://img.shields.io/badge/version-0.9.7--snapshot-blue)

MioVerify是一个根据 *[Yggdrasil 服务端技术规范](https://github.com/yushijinhun/authlib-injector/wiki/Yggdrasil-%E6%9C%8D%E5%8A%A1%E7%AB%AF%E6%8A%80%E6%9C%AF%E8%A7%84%E8%8C%83)* 实现的Web服务器。

有关Minecraft的authlib-injector技术，请详见 [yushijinhun/authlib-injector](https://github.com/yushijinhun/authlib-injector)。

## 实现功能

除了额外的注册登录API和features未实现，其他的基本已经实现。

本项目使用的技术：

* Spring Boot, Spring AOP, Spring Web
* JWT
* Redis
* Mybatis Plus (SQL)

测试工具：

* Postman
* HMCL
* Spring Test

## 使用教程

在 RELEASE 中获取最新的压缩包，解压到一个文件夹。

请**务必**修改`application.yml`里面的配置项，如果没有填写任何数据库信息，将会在运行目录自动生成和使用SQlite数据库。

**必须要先启动Redis服务器**并在配置里面填写好地址(默认可不填写)。

在项目目录下输入命令行启动对应jar即可。

## 待实现

* 动态的材质资源来源
* 注册登录API
* 后台管理网页
* ...

## 开发信息

本项目由作者个人独立开发完成，可能存在亿点bug。

如果对此项目感兴趣，可以联系作者QQ1153624453加入开发~~团队~~。
