webpackJsonp([1],{AIrM:function(t,e){},NHnr:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i=a("//Fk"),s=a.n(i),l=a("7+uW"),n={render:function(){this.$createElement;this._self._c;return this._m(0)},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("div",[e("div",{staticClass:"title",staticStyle:{"vertical-align":"center",height:"30px"}},[e("img",{attrs:{src:a("ZURh"),height:"30px"}}),this._v(" "),e("span",{staticStyle:{"line-height":"30px",height:"30px",overflow:"hidden"}},[this._v("Script Executor")])])])}]};var o={name:"App",components:{Header:a("VU/8")({name:"Header",data:function(){return{}}},n,!1,function(t){a("AIrM")},"data-v-440dad12",null).exports},data:function(){return{data:[],logTitle:"",logId:0,logVisible:!1,logs:[],interval:"",enableScroll:!0,groups:[],runTitle:"",runVisible:!1,runForm:{},activeGroup:"1",history:{},historyTitle:"",historyVisable:!1,page:0,size:5,historyTask:{}}},mounted:function(){var t=this,e=this;this.axios.get("tasks").then(function(a){for(var i in e.data=a.data,e.data)if("Y"===e.data[i].defaultGroup){t.activeGroup=e.data[i].id+"";break}var s=/(http:\/\/)?([A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*)/g;for(var l in e.data)for(var n in e.groups.push(e.data[l].group),e.data[l].tasks)e.data[l].tasks[n].desc=e.data[l].tasks[n].desc.replace(s,function(t,e,a){return'<a target="view_window" href="http://'+a+'">'+t+"</a>"})})},methods:{switchGroup:function(){},handleSizeChange:function(t){this.size=t,this.queryHistory()},handleCurrentChange:function(t){this.page=t,this.queryHistory()},handleHistoryClose:function(){this.historyTitle="",this.historyVisable=!1,this.history={}},showHistory:function(t){this.historyTitle="History: "+t.name,this.historyVisable=!0,this.historyTask=t,this.queryHistory()},queryHistory:function(){var t=this;this.axios.get("history/"+this.historyTask.id+"?size="+this.size+"&page="+this.page).then(function(e){t.history=e.data})},handleRunClose:function(){this.runVisible=!1,this.runTitle="",this.runForm={}},runTaskWithParam:function(t){var e=[];for(var a in t.data)e.push({paramId:t.data[a].id,value:t.data[a].value});this.run({taskId:t.id,params:e}),this.handleRunClose()},run:function(t){var e=this;this.axios.post("exec",t).then(function(a){for(var i in e.$message({message:a.data.message,type:"success",duration:5e3}),e.data)for(var s in e.data[i].tasks)e.data[i].tasks[s].id===t.taskId&&(e.data[i].tasks[s].lastRunTime=a.data.runTime)})},runTask:function(t){0===t.params.length?this.run({taskId:t.id}):(this.runForm={data:t.params,id:t.id},this.runTitle="Run: "+t.name,this.runVisible=!0)},handleLogClose:function(){this.logVisible=!1,this.logTitle="",this.logId=0,this.logs=[],this.enableScroll=!0,clearInterval(this.interval)},openLogs:function(t){this.getLogs(t);var e=this;this.interval=setInterval(function(){e.getLogs(t)},1e3),this.logTitle=t.name+" - Logs",this.logId=t.id,this.logVisible=!0},getLogs:function(t){var e=this;this.axios.get("logs?id="+t.id).then(function(t){e.logs=t.data;var a=document.querySelector(".log_end");null!=a&&e.enableScroll&&a.scrollIntoView({block:"start",behavior:"smooth"})})},scroll:function(){var t,e,a=document.getElementById("content");t=a.scrollTop,e=a.scrollHeight-a.offsetHeight,this.enableScroll=t>=e},allLogs:function(){window.open("allLogs?id="+this.logId)}}},r={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{attrs:{id:"app"}},[a("Header",{staticStyle:{"margin-bottom":"10px"}}),t._v(" "),a("div",{staticStyle:{"margin-top":"20px","vertical-align":"top"}},[a("el-tabs",{attrs:{type:"border-card",stretch:!0},on:{"tab-click":t.switchGroup},model:{value:t.activeGroup,callback:function(e){t.activeGroup=e},expression:"activeGroup"}},t._l(t.data,function(e){return a("el-tab-pane",{key:e.id,attrs:{name:e.id+"",label:e.group}},t._l(e.tasks,function(e,i){return a("el-card",{key:i,staticClass:"card",staticStyle:{"font-size":"13px"},attrs:{shadow:"hover"}},[a("div",{staticStyle:{height:"30px"},attrs:{slot:"header"},slot:"header"},[a("div",{staticStyle:{display:"inline-block",height:"30px","vertical-align":"center","line-height":"30px"}},[t._v(t._s(e.name))]),t._v(" "),a("div",{staticStyle:{display:"inline-block",float:"right","text-align":"right"}},[a("div",[a("el-button",{attrs:{icon:"el-icon-document",size:"small"},on:{click:function(a){t.openLogs(e)}}},[t._v("Logs\n                ")]),t._v(" "),a("el-button",{attrs:{type:"success",icon:"el-icon-s-promotion",size:"small"},on:{click:function(a){t.runTask(e)}}},[t._v("Run\n                ")])],1)])]),t._v(" "),a("div",{staticClass:"desc",domProps:{innerHTML:t._s(e.desc)}}),t._v(" "),a("div",{staticClass:"lastRunTime log",staticStyle:{float:"right"},on:{click:function(a){t.showHistory(e)}}},[a("i",{staticClass:"el-icon-timer"}),t._v("  "+t._s(null==e.lastRunTime?"Not yet running":e.lastRunTime)+"\n          ")])])}),1)}),1)],1),t._v(" "),a("el-dialog",{attrs:{title:t.logTitle,visible:t.logVisible,width:"80%","destroy-on-close":"","before-close":t.handleLogClose},on:{"update:visible":function(e){t.logVisible=e}}},[a("pre",{attrs:{id:"content"},on:{scroll:function(e){t.scroll()}}},[t._v("      "),t._l(t.logs,function(e,i){return a("div",{key:i,class:i===t.logs.length-1?"log_end":"log",staticStyle:{"margin-top":"2px","margin-bottom":"2px"},domProps:{innerHTML:t._s(e)}},[t._v("\n      ")])}),t._v("\n    ")],2),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{size:"small"},on:{click:function(e){t.allLogs()}}},[t._v("Full Log")]),t._v(" "),a("el-button",{attrs:{size:"small"},on:{click:t.handleLogClose}},[t._v("Close")])],1)]),t._v(" "),a("el-dialog",{attrs:{title:t.runTitle,visible:t.runVisible,width:"600px","destroy-on-close":"","before-close":t.handleRunClose},on:{"update:visible":function(e){t.runVisible=e}}},[a("el-form",{ref:"form",attrs:{model:t.runForm,"label-width":"150px"}},t._l(t.runForm.data,function(e){return a("el-form-item",{key:e.id,attrs:{label:e.name}},[a("div",{staticStyle:{"margin-right":"80px"}},["TEXT"===e.type?a("el-input",{attrs:{clearable:"",size:"small"},model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}}):t._e(),t._v(" "),"PASSWORD"===e.type?a("el-input",{attrs:{type:"password","show-password":"",size:"small"},model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}}):t._e(),t._v(" "),"NUMBER"===e.type?a("el-input-number",{attrs:{size:"small"},model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}}):t._e(),t._v(" "),"BOOLEAN"===e.type?a("el-switch",{model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}}):t._e(),t._v(" "),"DROPDOWN"===e.type?a("el-select",{attrs:{size:"small"},model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}},t._l(e.availableValue,function(t){return a("el-option",{key:t,attrs:{value:t,label:t}})}),1):t._e()],1)])}),1),t._v(" "),a("div",{staticStyle:{"text-align":"right","font-size":"15px"}},[a("el-button",{attrs:{size:"small"},on:{click:t.handleRunClose}},[t._v("Cancel")]),t._v(" "),a("el-button",{attrs:{type:"success",size:"small"},on:{click:function(e){t.runTaskWithParam(t.runForm)}}},[t._v("Run")])],1)],1),t._v(" "),a("el-dialog",{attrs:{title:t.historyTitle,visible:t.historyVisable,width:"70%","destroy-on-close":"","before-close":t.handleHistoryClose},on:{"update:visible":function(e){t.historyVisable=e}}},[a("el-table",{staticStyle:{width:"100%"},attrs:{data:t.history.data}},[a("el-table-column",{attrs:{prop:"user",label:"User",width:"120"}}),t._v(" "),a("el-table-column",{attrs:{prop:"ip",label:"IP",width:"150"}}),t._v(" "),a("el-table-column",{attrs:{prop:"datetime",label:"Datetime",width:"150"}}),t._v(" "),a("el-table-column",{attrs:{label:"Parameters"},scopedSlots:t._u([{key:"default",fn:function(e){return t._l(e.row.params,function(e){return a("el-tag",{key:e.param,staticStyle:{margin:"5px"}},[t._v(t._s(e.param)+": "+t._s(e.value))])})}}])})],1),t._v(" "),a("el-pagination",{attrs:{layout:"sizes, prev, pager, next",total:t.history.totalSize,"page-sizes":[5,10,15,20],"page-size":5},on:{"size-change":t.handleSizeChange,"current-change":t.handleCurrentChange}})],1)],1)},staticRenderFns:[]};var u=a("VU/8")(o,r,!1,function(t){a("gY2z")},null,null).exports,c=a("zL8q"),d=a.n(c),h=a("mtWM"),p=a.n(h);l.default.use(d.a),l.default.config.productionTip=!1;p.a.defaults.baseURL="public/api",p.a.defaults.withCredentials=!0,l.default.prototype.axios=p.a,p.a.interceptors.response.use(function(t){return t},function(t){return 500===t.response.data.status&&l.default.prototype.$notify({title:"Error",message:t.response.data.message,type:"error"}),s.a.reject(t)}),new l.default({el:"#app",components:{App:u},template:"<App/>"})},ZURh:function(t,e,a){t.exports=a.p+"static/img/favicon.5bde6bf.png"},gY2z:function(t,e){}},["NHnr"]);
//# sourceMappingURL=app.f270f5e811c058b499bb.js.map