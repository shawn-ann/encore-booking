<template>
  <div class="dashboard-container">
    <!--    <component :is="currentRole" />-->

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <el-header>欢迎登录</el-header>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
// import adminDashboard from './admin'
// import editorDashboard from './editor'

export default {
  name: 'Dashboard',
  // components: { adminDashboard, editorDashboard },
  data() {
    return {
      currentRole: 'adminDashboard'
    }
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    if (!this.roles.includes('admin')) {
      this.currentRole = 'editorDashboard'
    }
  }
}
</script>
