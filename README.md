# Android 日历提醒软件
快要毕业答辩了，课题是做一款Android日历提醒软件，最近一直在改论文，代码很久之前就完成了，虽然很多方面写的不好，但还是决定分享出来，希望可以帮助到一些同学，大家也可以对我有缺陷的地方提出建议。

因为这个项目主要以练手为主，所以在界面和代码编写上参考了很多开源的项目，以实现功能为主。

GitHub项目地址:[https://github.com/Werb/MyCalendarDemo](https://github.com/Werb/MyCalendarDemo)

### 主界面
* 整体风格是参考Google日历
* 主界面可以根据不同的日期滑动选择查看概要日程信息
* 侧滑菜单可以分类按照某一周或某一天查看日程信息
* 日历和日程的参考GitHub上的开源项目[AgendaCalendarView](https://github.com/Tibolte/AgendaCalendarView)，然后根据自己情况，修改代码，配合数据库实现动态数据显示

<img src="/screenshots/s1.jpg" alt="screenshot" title="主界面" width="270" height="486" /> <img src="/screenshots/s3.jpg" alt="screenshot" title="侧滑菜单界面" width="270" height="486" />

### 分类查看(按周或天查看)
<img src="/screenshots/s4.jpg" alt="screenshot" title="某一天日程信息" width="270" height="486" /> <img src="/screenshots/s5.jpg" alt="screenshot" title="某一周日程信息" width="270" height="486" />

### 添加日程提醒和日程信息详情界面
* 这里界面完全参考Google日程添加日程界面
* 选择日程活动提醒时间、重复次数、是否震动、提醒铃声、显示颜色等
* 查看详情界面，同时可以删除和修改日程信息

<img src="/screenshots/s2.jpg" alt="screenshot" title="添加日程提醒" width="270" height="486" /> <img src="/screenshots/s6.jpg" alt="screenshot" title="日程信息详情界面" width="270" height="486" />

### 日程到点提醒
* 提醒只是调用系统弹窗，伴随震动和铃声
* 这里涉及Android6.0权限问题，代码中已有相应设置，但由于国内厂商对手机权限修改过多，如果存在不能提醒情况，
请手动在设置中打开相应权限
* 这里处理的不是很好，有更好的处理方法，大家可以联系告诉我

<img src="/screenshots/s7.jpg" alt="screenshot" title="日程到点提醒" width="270" height="486" />

### License
* 再次感谢开源项目[AgendaCalendarView](https://github.com/Tibolte/AgendaCalendarView)的帮助
* 同时希望可以帮助到其他人

====



      Copyright 2016 Werb

      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.

### Contact Me
* Email: 1025004680@qq.com
* Weibo: [UMR80](http://weibo.com/singerwannber )
* GitHub: [Werb](https://github.com/Werb/MyCalendarDemo)
