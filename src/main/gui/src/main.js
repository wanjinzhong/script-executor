// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import ElementUI from "element-ui";
import axios from "axios"
Vue.use(ElementUI);
Vue.config.productionTip = false
// let apiUrl = "http://localhost:8999/exec/public/api"
let apiUrl = "public/api"
axios.defaults.baseURL = apiUrl;
axios.defaults.withCredentials = true;
Vue.prototype.axios = axios;
axios.interceptors.response.use(function (response) {
  return response;
}, function (error) {
  if (error.response.data.status == 500) {
    Vue.prototype.$notify({
      title: "Error",
      message: error.response.data.message,
      type: "error"
    });
  }
  return Promise.reject(error);
});
/* eslint-disable no-new */
new Vue({
  el: '#app',
  components: { App },
  template: '<App/>'
})
