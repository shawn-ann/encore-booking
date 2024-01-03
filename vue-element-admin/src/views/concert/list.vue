<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.name"
        placeholder="名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        添加
      </el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="table-expand">
            <el-row>
              <el-form-item label="场次">
                <el-tag v-for="item in props.row.sessionList" :key="item.id">{{ item.name }}</el-tag>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="票档">
                <el-tag v-for="item in props.row.ticketCategoryList" :key="item.id">{{ item.name }}</el-tag>
              </el-form-item>
            </el-row>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" label="名称">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="240">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">
            编辑
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="70px"
        style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="演唱会" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>

        <el-form-item label="场次" prop="sessionList">
          <el-row
            v-for="session in temp.sessionList"
            :key="session.id"
          >
            <el-col :span="16">
              <el-input v-model="session.name" />
            </el-col>
            <el-col :span="8">
              <el-button icon="el-icon-minus" @click.prevent="removeSession(session)" />
              <el-button icon="el-icon-plus" style="margin-left: 0px;" @click="addSession()" />
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item label="票档" prop="ticketCategoryList">
          <el-row
            v-for="ticket in temp.ticketCategoryList"
            :key="ticket.id"
          >
            <el-col :span="16">
              <el-input v-model="ticket.name" />
            </el-col>
            <el-col :span="8">
              <el-button icon="el-icon-minus" @click.prevent="removeTicketCategory(ticket)" />
              <el-button icon="el-icon-plus" style="margin-left: 0px;" @click="addTicketCategory()" />
            </el-col>
          </el-row>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, createItem, updateItem, deleteItem } from '@/api/concert'
import Pagination from '@/components/Pagination'

export default {
  name: 'ConcertList',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'info',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        name: ''
      },
      temp: {
        id: undefined,
        name: undefined,
        sessionList: [{
          id: '',
          name: ''
        }],
        ticketCategoryList: [{
          id: '',
          name: ''
        }]
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '更新',
        create: '添加'
      },
      rules: {
        name: [{ required: true, message: '请输入演唱会名称' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.list = response.data.content
        this.total = response.data.totalElements
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetTemp() {
      this.temp = {
        id: undefined,
        name: undefined,
        sessionList: [{
          id: '',
          name: ''
        }],
        ticketCategoryList: [{
          id: '',
          name: ''
        }]
      }
    },
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    validateForm() {
      for (const index in this.temp.sessionList) {
        const item = this.temp.sessionList[index]
        if (!item.name) {
          this.$message('请输入场次')
          return false
        }
      }
      for (const index in this.temp.ticketCategoryList) {
        const item = this.temp.ticketCategoryList[index]
        if (!item.name) {
          this.$message('请输入票档')
          return false
        }
      }
      return true
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid && this.validateForm()) {
          createItem(this.temp).then(() => {
            this.handleFilter()
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid && this.validateForm()) {
          const tempData = Object.assign({}, this.temp)
          updateItem(tempData).then(() => {
            this.handleFilter()
            this.dialogFormVisible = false
            this.$notify({
              title: '成功',
              message: '更新成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleDelete(id, index) {
      deleteItem(id).then(() => {
        this.handleFilter()
        this.dialogFormVisible = false
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    },
    removeSession(item) {
      const index = this.temp.sessionList.indexOf(item)
      if (this.temp.sessionList.length === 1) {
        return
      }
      if (index !== -1) {
        this.temp.sessionList.splice(index, 1)
      }
    },
    addSession() {
      this.temp.sessionList.push({
        name: '',
        id: undefined
      })
    },
    removeTicketCategory(item) {
      const index = this.temp.ticketCategoryList.indexOf(item)
      if (this.temp.ticketCategoryList.length === 1) {
        return
      }
      if (index !== -1) {
        this.temp.ticketCategoryList.splice(index, 1)
      }
    },
    addTicketCategory() {
      this.temp.ticketCategoryList.push({
        name: '',
        id: undefined
      })
    }
  }
}
</script>

<style scoped>
.edit-input {
  padding-right: 100px;
}

.cancel-btn {
  position: absolute;
  right: 15px;
  top: 10px;
}
</style>
