webpackJsonp([1],{AIrM:function(t,e){},NHnr:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var n=a("//Fk"),s=a.n(n),i=a("7+uW"),l={render:function(){this.$createElement;this._self._c;return this._m(0)},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("div",[e("div",{staticClass:"title",staticStyle:{"vertical-align":"center",height:"30px"}},[e("img",{attrs:{src:a("ZURh"),height:"30px"}}),this._v(" "),e("span",{staticStyle:{"line-height":"30px",height:"30px",overflow:"hidden"}},[this._v("Script Executor")])])])}]};var o={name:"App",components:{Header:a("VU/8")({name:"Header",data:function(){return{}}},l,!1,function(t){a("AIrM")},"data-v-440dad12",null).exports},data:function(){return{data:[],logTitle:"",logId:0,logVisible:!1,logs:[],interval:"",enableScroll:!0,groups:[],runTitle:"",runVisible:!1,runForm:{},activeGroup:[]}},mounted:function(){var t=this,e=this;this.axios.get("tasks").then(function(a){for(var n in e.data=a.data.data,e.data)"Y"===e.data[n].expand&&t.activeGroup.push(e.data[n].id);var s=/(http:\/\/)?([A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*)/g;for(var i in e.data)for(var l in e.groups.push(e.data[i].group),e.data[i].tasks)e.data[i].tasks[l].desc=e.data[i].tasks[l].desc.replace(s,function(t,e,a){return'<a target="view_window" href="http://'+a+'">'+t+"</a>"})})},methods:{handleRunClose:function(){this.runVisible=!1,this.runTitle="",this.runForm={}},runTaskWithParam:function(t){var e=[];for(var a in t.data)e.push({paramId:t.data[a].id,value:t.data[a].value});this.run({taskId:t.id,params:e}),this.handleRunClose()},run:function(t){var e=this;this.axios.post("exec",t).then(function(a){for(var n in e.$message({message:a.data.message,type:"success",duration:5e3}),e.data)for(var s in e.data[n].tasks)e.data[n].tasks[s].id===t.taskId&&(e.data[n].tasks[s].lastRunTime=a.data.runTime)})},runTask:function(t){0===t.params.length?this.run({taskId:t.id}):(this.runForm={data:t.params,id:t.id},this.runTitle="Run: "+t.name,this.runVisible=!0)},handleLogClose:function(){this.logVisible=!1,this.logTitle="",this.logId=0,this.logs=[],this.enableScroll=!0,clearInterval(this.interval)},openLogs:function(t){this.getLogs(t);var e=this;this.interval=setInterval(function(){e.getLogs(t)},1e3),this.logTitle=t.name+" - Logs",this.logId=t.id,this.logVisible=!0},getLogs:function(t){var e=this;this.axios.get("logs?id="+t.id).then(function(t){e.logs=t.data;var a=document.querySelector(".log_end");null!=a&&e.enableScroll&&a.scrollIntoView({block:"start",behavior:"smooth"})})},scroll:function(){var t,e,a=document.getElementById("content");t=a.scrollTop,e=a.scrollHeight-a.offsetHeight,this.enableScroll=t>=e},allLogs:function(){window.open("allLogs?id="+this.logId)}}},r={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{attrs:{id:"app"}},[a("Header",{staticStyle:{"margin-bottom":"10px"}}),t._v(" "),a("div",{staticStyle:{"background-color":"rgba(137,138,116,0.29)",width:"100%",height:"1px"}}),t._v(" "),a("div",{staticStyle:{"margin-top":"10px","vertical-align":"top"}},[a("el-collapse",{model:{value:t.activeGroup,callback:function(e){t.activeGroup=e},expression:"activeGroup"}},t._l(t.data,function(e){return a("el-collapse-item",{key:e.id,attrs:{title:e.group,name:e.id}},t._l(e.tasks,function(e,n){return a("el-card",{key:n,staticClass:"card",attrs:{shadow:"hover"}},[a("div",{attrs:{slot:"header"},slot:"header"},[t._v("\n            "+t._s(e.name)+"\n            "),a("div",{staticStyle:{float:"right","font-size":"15px"}},[a("div",[a("el-button",{attrs:{icon:"el-icon-document",size:"small"},on:{click:function(a){t.openLogs(e)}}},[t._v("Logs\n              ")]),t._v(" "),a("el-button",{attrs:{type:"success",icon:"el-icon-s-promotion",size:"small"},on:{click:function(a){t.runTask(e)}}},[t._v("Run\n              ")])],1)])]),t._v(" "),a("div",{staticClass:"desc",domProps:{innerHTML:t._s(e.desc)}}),t._v(" "),null!=e.lastRunTime?a("div",{staticClass:"lastRunTime desc",staticStyle:{float:"right"}},[a("i",{staticClass:"el-icon-timer"}),t._v(" "+t._s(e.lastRunTime))]):t._e()])}),1)}),1)],1),t._v(" "),a("el-dialog",{attrs:{title:t.logTitle,visible:t.logVisible,width:"80%","destroy-on-close":"","before-close":t.handleLogClose},on:{"update:visible":function(e){t.logVisible=e}}},[a("pre",{attrs:{id:"content"},on:{scroll:function(e){t.scroll()}}},[t._v("      "),t._l(t.logs,function(e,n){return a("div",{key:n,class:n===t.logs.length-1?"log_end":"log",staticStyle:{"margin-top":"2px","margin-bottom":"2px"},domProps:{innerHTML:t._s(e)}},[t._v("\n      ")])}),t._v("\n    ")],2),t._v(" "),a("span",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[a("el-button",{attrs:{size:"small"},on:{click:function(e){t.allLogs()}}},[t._v("Full Log")]),t._v(" "),a("el-button",{attrs:{size:"small"},on:{click:t.handleLogClose}},[t._v("Close")])],1)]),t._v(" "),a("el-dialog",{attrs:{title:t.runTitle,visible:t.runVisible,width:"50%","destroy-on-close":"","before-close":t.handleRunClose},on:{"update:visible":function(e){t.runVisible=e}}},[a("el-form",{ref:"form",attrs:{model:t.runForm,"label-width":"80px"}},t._l(t.runForm.data,function(e){return a("el-form-item",{key:e.id,attrs:{label:e.name}},["TEXT"===e.type?a("el-input",{attrs:{clearable:""},model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}}):t._e(),t._v(" "),"PASSWORD"===e.type?a("el-input",{attrs:{type:"password","show-password":""},model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}}):t._e(),t._v(" "),"NUMBER"===e.type?a("el-input-number",{model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}}):t._e(),t._v(" "),"BOOLEAN"===e.type?a("el-switch",{model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}}):t._e(),t._v(" "),"DROPDOWN"===e.type?a("el-select",{model:{value:e.value,callback:function(a){t.$set(e,"value",a)},expression:"p.value"}},t._l(e.availableValue,function(t){return a("el-option",{key:t,attrs:{value:t,label:t}})}),1):t._e()],1)}),1),t._v(" "),a("div",{staticStyle:{"text-align":"right","font-size":"15px"}},[a("el-button",{on:{click:t.handleRunClose}},[t._v("Cancel")]),t._v(" "),a("el-button",{attrs:{type:"success"},on:{click:function(e){t.runTaskWithParam(t.runForm)}}},[t._v("Run")])],1)],1)],1)},staticRenderFns:[]};var u=a("VU/8")(o,r,!1,function(t){a("blgA")},null,null).exports,c=a("zL8q"),d=a.n(c),p=a("mtWM"),v=a.n(p);i.default.use(d.a),i.default.config.productionTip=!1;v.a.defaults.baseURL="public/api",v.a.defaults.withCredentials=!0,i.default.prototype.axios=v.a,v.a.interceptors.response.use(function(t){return t},function(t){return 500==t.response.data.status&&i.default.prototype.$notify({title:"Error",message:t.response.data.message,type:"error"}),s.a.reject(t)}),new i.default({el:"#app",components:{App:u},template:"<App/>"})},ZURh:function(t,e,a){t.exports=a.p+"static/img/favicon.5bde6bf.png"},blgA:function(t,e){}},["NHnr"]);
//# sourceMappingURL=app.38118e1371db50a97dae.js.map