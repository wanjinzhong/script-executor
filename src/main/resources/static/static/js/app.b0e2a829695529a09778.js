webpackJsonp([1],{AIrM:function(t,e){},NHnr:function(t,e,n){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var a=n("//Fk"),s=n.n(a),i=n("7+uW"),r={render:function(){this.$createElement;this._self._c;return this._m(0)},staticRenderFns:[function(){var t=this.$createElement,e=this._self._c||t;return e("div",[e("div",{staticClass:"title",staticStyle:{"vertical-align":"center",height:"30px"}},[e("img",{attrs:{src:n("ZURh"),height:"30px"}}),this._v(" "),e("span",{staticStyle:{"line-height":"30px",height:"30px",overflow:"hidden"}},[this._v("Script Executor")])])])}]};var c={name:"App",components:{Header:n("VU/8")({name:"Header",data:function(){return{}}},r,!1,function(t){n("AIrM")},"data-v-440dad12",null).exports},data:function(){return{data:[]}},mounted:function(){var t=this;this.axios.get("categories").then(function(e){t.data=e.data.data})},methods:{run:function(t){var e=this;this.axios.get("exec?id="+t).then(function(t){"OK"===t.data?e.$message.success("OK, Let`s run..."):e.$message.error("Failed to run this script...")})}}},o={render:function(){var t=this,e=t.$createElement,n=t._self._c||e;return n("div",{attrs:{id:"app"}},[n("Header"),t._v(" "),n("div",t._l(t.data,function(e,a){return n("el-card",{key:a,staticClass:"card"},[n("div",{staticClass:"item-content"},[n("div",{staticClass:"name"},[t._v(t._s(e.name))]),t._v(" "),n("div",{staticClass:"desc"},[t._v(t._s(e.desc))])]),t._v(" "),n("div",{staticClass:"run-div"},[n("el-button",{attrs:{type:"success",icon:"el-icon-s-promotion"},on:{click:function(n){t.run(e.id)}}},[t._v("Run")])],1)])}),1)],1)},staticRenderFns:[]};var u=n("VU/8")(c,o,!1,function(t){n("qeQR")},null,null).exports,d=n("zL8q"),l=n.n(d),p=n("mtWM"),f=n.n(p);i.default.use(l.a),i.default.config.productionTip=!1;f.a.defaults.baseURL="public/api",f.a.defaults.withCredentials=!0,i.default.prototype.axios=f.a,f.a.interceptors.response.use(function(t){return t},function(t){return 500==t.response.data.status&&i.default.prototype.$notify({title:"Error",message:t.response.data.message,type:"error"}),s.a.reject(t)}),new i.default({el:"#app",components:{App:u},template:"<App/>"})},ZURh:function(t,e,n){t.exports=n.p+"static/img/favicon.5bde6bf.png"},qeQR:function(t,e){}},["NHnr"]);
//# sourceMappingURL=app.b0e2a829695529a09778.js.map