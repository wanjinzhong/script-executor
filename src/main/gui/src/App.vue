<template>
  <div id="app">
    <Header style="margin-bottom: 10px;"></Header>
    <div style="margin-top: 20px; vertical-align: top">
      <el-tabs v-model="activeGroup" @tab-click="switchGroup" type="border-card" :stretch="true">
        <el-tab-pane v-for="group in data" :key="group.id" :name="group.id + ''" :label="group.group" >
          <el-card v-for="(k, i) in group.tasks" :key="i" class="card" shadow="hover" style="font-size: 13px">
            <div slot="header" style="height: 30px">
              <div style="display:inline-block; height: 30px; vertical-align: center; line-height: 30px">{{k.name}}</div>
              <div style="display:inline-block; float: right; text-align: right">
                <div>
                  <el-button icon="el-icon-document" size="small"
                             v-on:click="openLogs(k)">Logs
                  </el-button>
                  <el-button type="success" icon="el-icon-s-promotion" size="small"
                             v-on:click="runTask(k)">Run
                  </el-button>
                </div>
              </div>
            </div>
            <div class="desc" v-html="k.desc"></div>
            <div class="lastRunTime log" style="float: right" v-on:click="showHistory(k)">
              <i class="el-icon-timer"/>&nbsp;&nbsp;{{k.lastRunTime == null? "Not yet running" : k.lastRunTime}}
            </div>
          </el-card>
        </el-tab-pane>
      </el-tabs>
    </div>
    <el-dialog
      :title="logTitle"
      :visible.sync="logVisible"
      width="80%"
      destroy-on-close
      :before-close="handleLogClose">
      <pre
        id="content" v-on:scroll="scroll()">
        <div v-for="(log, i) in logs" style="margin-top: 2px; margin-bottom: 2px; " :key="i"
             :class="i === (logs.length - 1)? 'log_end':'log'" v-html="log">
        </div>
      </pre>
      <span slot="footer" class="dialog-footer">
        <el-button @click="allLogs()" size="small">Full Log</el-button>
        <el-button @click="handleLogClose" size="small">Close</el-button>
    </span>
    </el-dialog>
    <el-dialog
      :title="runTitle"
      :visible.sync="runVisible"
      width="600px"
      destroy-on-close
      :before-close="handleRunClose">
      <el-form ref="form" :model="runForm" label-width="150px">
        <el-form-item v-for="p in runForm.data" :key="p.id" :label="p.name">
          <div style="margin-right: 80px">
          <el-input v-if="p.type === 'TEXT'" v-model="p.value" clearable size="small"></el-input>
          <el-input v-if="p.type === 'PASSWORD'" type="password" v-model="p.value" show-password  size="small"></el-input>
          <el-input-number v-if="p.type === 'NUMBER'" v-model="p.value"  size="small"></el-input-number>
          <el-switch v-if="p.type === 'BOOLEAN'" v-model="p.value"></el-switch>
          <el-select v-if="p.type === 'DROPDOWN'" v-model="p.value"  size="small">
            <el-option v-for="v in p.availableValue" :key="v" :value="v" :label="v"/>
          </el-select>
          </div>
        </el-form-item>
      </el-form>
      <div style="text-align:right; font-size: 15px">
        <el-button @click="handleRunClose"  size="small">Cancel</el-button>
        <el-button @click="runTaskWithParam(runForm)" type="success"  size="small">Run</el-button>
      </div>
    </el-dialog>
    <el-dialog
      :title="historyTitle"
      :visible.sync="historyVisable"
      width="70%"
      destroy-on-close
      :before-close="handleHistoryClose">
      <el-table :data="history.data" style="width: 100%">
        <el-table-column prop="user" label="User" width="150"/>
        <el-table-column prop="ip" label="IP" width="150"/>
        <el-table-column prop="datetime" label="Datetime" width="180"/>
        <el-table-column label="Parameters">
          <template slot-scope="scope">
            <el-tag v-for="p in scope.row.params" :key="p.param" style="margin: 5px">{{p.param}}: {{p.value}}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination layout="sizes, prev, pager, next"
                     :total="history.totalSize" :page-sizes="[5, 10, 15, 20]" :page-size="5"
                     @size-change="handleSizeChange"
                     @current-change="handleCurrentChange"></el-pagination>
    </el-dialog>
  </div>
</template>

<script>

  import Header from "./components/Header"

  export default {
    name: 'App',
    components: {Header},
    data() {
      return {
        data: [],
        logTitle: "",
        logId: 0,
        logVisible: false,
        logs: [],
        interval: "",
        enableScroll: true,
        groups: [],
        runTitle: "",
        runVisible: false,
        runForm: {},
        activeGroup: '1',
        history: {},
        historyTitle: "",
        historyVisable: false,
        page: 0,
        size: 5,
        historyTask: {}
      }
    },
    mounted() {
      const that = this
      this.axios.get("tasks").then(res => {
        that.data = res.data;
        for (let i in that.data) {
          if  (that.data[i].defaultGroup === 'Y') {
            this.activeGroup = that.data[i].id + "";
            break;
          }
        }
        let reg = /(http:\/\/)?([A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\':+!]*([^<>\"\"])*)/g;
        for (let i in that.data) {
          that.groups.push(that.data[i].group)
          for (let j in that.data[i].tasks) {
            that.data[i].tasks[j].desc = that.data[i].tasks[j].desc.replace(reg, function (a, b, c) {
              return '<a target="view_window" href="http://' + c + '"\>' + a + '</a>';
            });
          }
        }
      })
    },
    methods: {
      switchGroup() {

      },
      handleSizeChange(val) {
        this.size = val
        this.queryHistory();

      },
      handleCurrentChange(val) {
        this.page = val
        this.queryHistory();
      },
      handleHistoryClose() {
        this.historyTitle = ""
        this.historyVisable = false
        this.history = {}
      },
      showHistory(task) {
        this.historyTitle = "History: " + task.name
        this.historyVisable = true;
        this.historyTask = task;
        this.queryHistory();
      },
      queryHistory() {
        this.axios.get("history/" + this.historyTask.id + "?size="+this.size+"&page="+this.page).then(res => {
          this.history = res.data
        });
      },
      handleRunClose() {
        this.runVisible = false
        this.runTitle = ""
        this.runForm = {}
      },
      runTaskWithParam(form) {
        const params = [];
        for (let i in form.data) {
          params.push({"paramId": form.data[i].id, "value": form.data[i].value});
        }
        this.run({"taskId": form.id, "params": params})
        this.handleRunClose()
      },
      run(form) {
        this.axios.post("exec", form).then(res => {
          this.$message({
            message: res.data.message,
            type: "success",
            duration: 5000
          })
          for (let i in this.data) {
            for (let j in this.data[i].tasks) {
              if (this.data[i].tasks[j].id === form.taskId) {
                this.data[i].tasks[j].lastRunTime = res.data.runTime;
              }
            }
          }
        })
      },
      runTask(task) {
        if (task.params.length === 0) {
          this.run({"taskId": task.id})
        } else {
          this.runForm = {"data": task.params, "id": task.id}
          this.runTitle = "Run: " + task.name
          this.runVisible = true
        }
      },
      handleLogClose() {
        this.logVisible = false;
        this.logTitle = "";
        this.logId = 0;
        this.logs = [];
        this.enableScroll = true;
        clearInterval(this.interval)
      },
      openLogs(category) {
        this.getLogs(category)
        const that = this
        this.interval = setInterval(function () {
          that.getLogs(category)
        }, 1000)
        this.logTitle = category.name + " - Logs"
        this.logId = category.id
        this.logVisible = true
      },
      getLogs(category) {
        this.axios.get('logs?id=' + category.id).then(res => {
          // for (let i in res.data) {
            // res.data[i] = res.data[i].replace(/ /g, "&nbsp;");
            // res.data[i] = res.data[i].replace(/\t/g, "&nbsp;&nbsp;&nbsp;&nbsp;");
          // }
          this.logs = res.data
          const ele = document.querySelector(".log_end");
          if (ele != null && this.enableScroll) {
            ele.scrollIntoView({
              block: 'start',
              behavior: 'smooth'
            })
          }
        })
      },
      scroll() {
        var panel = document.getElementById("content");
        var scrollTop, maxScroll, minScroll = 0;
        scrollTop = panel.scrollTop;
        maxScroll = panel.scrollHeight - panel.offsetHeight;
        this.enableScroll = scrollTop >= maxScroll
      },
      allLogs() {
        window.open('allLogs?id=' + this.logId)
      }
    }
  }
</script>

<style>
  #app {
    font-family: 'Avenir', Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    margin: 20px;
    min-width: 800px;
  }

  .card {
    display: inline-block;
    vertical-align: top;
    width: 500px;
    margin: 20px;
  }

  .desc {
    font-style: italic;
    color: dimgrey;
    line-height: 1.7;
  }

  .log_content textarea {
    height: 100%;
  }

  #content {
    height: 500px;
    width: 98%;
    overflow: auto;
    border: rgba(105, 105, 105, 0.31) solid 1px;
    border-radius: 3px;
    padding: 10px;
  }
  .log {
    color: grey;
  }
  .log:hover {
    cursor: pointer;
    color: dimgrey;
  }
</style>
