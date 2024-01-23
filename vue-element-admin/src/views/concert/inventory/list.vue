<template>
  <div class="app-container">
    <div class="filter-container">
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

      <el-table-column align="center" label="演唱会">
        <template slot-scope="scope">
          <span>{{ scope.row.concertName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="场次">
        <template slot-scope="scope">
          <span>{{ scope.row.sessionName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="票档">
        <template slot-scope="scope">
          <span>{{ scope.row.ticketCategoryName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="总库存">
        <template slot-scope="scope">
          <span>{{ scope.row.totalQuantity }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="未分配库存" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.unallocatedQuantity }} </span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="剩余库存" width="180">
        <template slot-scope="scope">
          <el-tag>
            <span>{{ scope.row.remainingQuantity }} </span>
          </el-tag>
        </template>
      </el-table-column>

      <!--      <el-table-column align="center" label="状态">-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-tag>-->
      <!--            <span>{{ scope.row.status | statusFilter }}</span>-->
      <!--          </el-tag>-->
      <!--        </template>-->
      <!--      </el-table-column>-->

      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <!--          <el-button v-if="scope.row.status == 'ACTIVE'" type="info" size="mini" @click="inactive(scope.row.id)">-->
          <!--            下架-->
          <!--          </el-button>-->
          <!--          <el-button-->
          <!--            v-if="scope.row.status == 'INACTIVE'"-->
          <!--            type="success"-->
          <!--            size="mini"-->
          <!--            @click="active(scope.row.id)"-->
          <!--          >-->
          <!--            上架-->
          <!--          </el-button>-->
          <!--          <el-button-->
          <!--            v-if="scope.row.status == 'INACTIVE'"-->
          <!--            size="mini"-->
          <!--            type="danger"-->
          <!--            @click="handleDelete(scope.row.id,$index)"-->
          <!--          >-->
          <!--            删除-->
          <!--          </el-button>-->

          <el-button
            size="mini"
            type="primary"
            @click="handleQuantityUpdate(scope.row.id,'ADD')"
          >
            添加库存
          </el-button>
          <el-button
            size="mini"
            type="danger"
            @click="handleQuantityUpdate(scope.row.id,'MINUS')"
          >
            减少库存
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
        :model="temp"
        :rules="rules"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="演唱会" prop="concertId">
          <el-select
            v-model="temp.concertId"
            filterable
            placeholder="请选择"
            class="filter-item"
            @change="handConcertDropdownChange()"
          >
            <el-option
              v-for="item in concertDropdownOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="场次" prop="sessionId">
          <el-select
            v-model="temp.sessionId"
            filterable
            placeholder="请选择"
            class="filter-item"
          >
            <el-option
              v-for="item in sessionDropdownOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="票档" prop="ticketCategoryId">
          <el-select
            v-model="temp.ticketCategoryId"
            filterable
            placeholder="请选择"
            class="filter-item"
          >
            <el-option
              v-for="item in ticketCategoryDropdownOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="dialogStatus == 'create'" label="数量" prop="quantity">
          <el-input v-model.number="temp.quantity" autocomplete="off" />
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="createData()">
          确认
        </el-button>
      </div>
    </el-dialog>
    <el-dialog :title="textMap[stock.operation]" :visible.sync="stockDialogFormVisible">
      <el-form
        ref="stockDataForm"
        :rules="rules"
        :model="stock"
        label-position="left"
        label-width="70px"
        style="width: 400px; margin-left:50px;"
      >
        <el-form-item label="数量" prop="quantity" required>
          <el-input-number v-model="stock.quantity" :min="1" @change="handleChange" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="stockDialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="updateQuantityData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, createItem, deleteItem, updateQuantity, statusActive, statusInactive } from '@/api/inventory'
import Pagination from '@/components/Pagination'
import { fetchDropdown as fetchConcertDropdown } from '@/api/concert'
import { fetchDropdown as fetchSessionDropdown } from '@/api/session'
import { fetchDropdown as fetchTicketCategoryDropdown } from '@/api/ticket-category'

export default {
  name: 'ConcertList',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        ACTIVE: '上架',
        INACTIVE: '下架'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      concertDropdownOptions: [],
      sessionDropdownOptions: [],
      ticketCategoryDropdownOptions: [],
      listQuery: {
        page: 1,
        limit: 20,
        concertId: null
      },
      temp: {
        id: undefined,
        concertId: undefined,
        sessionId: undefined,
        ticketCategoryId: undefined,
        quantity: undefined
      },
      stock: {
        id: undefined,
        operation: undefined,
        quantity: undefined
      },
      dialogFormVisible: false,
      stockDialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '更新',
        create: '添加',
        ADD: '添加',
        MINUS: '扣减'
      },
      rules: {
        ticketCategoryId: [{ required: true, message: '请选择票档' }],
        concertId: [{ required: true, message: '请选择演唱会' }],
        sessionId: [{ required: true, message: '请选择场次' }],
        quantity: [
          { required: true, message: '请输入数量' },
          { type: 'number', message: '数量必须为数字值' }]
      }
    }
  },
  created() {
    const concertId = this.$route.params && this.$route.params.concertId
    this.listQuery.concertId = concertId
    this.getList()
  },
  methods: {
    initDropdown() {
      fetchConcertDropdown().then(response => {
        this.concertDropdownOptions = response.data
      })
    },
    handConcertDropdownChange() {
      const concertId = this.temp.concertId
      if (!concertId) {
        return
      }
      this.temp.sessionId = undefined
      this.temp.ticketCategoryId = undefined
      fetchSessionDropdown(concertId).then(response => {
        this.sessionDropdownOptions = response.data
      })
      fetchTicketCategoryDropdown(concertId).then(response => {
        this.ticketCategoryDropdownOptions = response.data
      })
    },
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
        concertId: undefined,
        sessionId: undefined,
        ticketCategoryId: undefined,
        quantity: undefined
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
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
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
    handleQuantityUpdate(id, opt) {
      this.stock = {
        id: id,
        operation: opt,
        quantity: 0
      }
      this.stockDialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['stockDataForm'].clearValidate()
      })
    },
    updateQuantityData() {
      this.$refs['stockDataForm'].validate((valid) => {
        if (valid) {
          updateQuantity(this.stock).then(() => {
            this.handleFilter()
            this.stockDialogFormVisible = false
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
    active(id) {
      statusActive(id).then(() => {
        this.handleFilter()
        this.dialogFormVisible = false
        this.$notify({
          title: '成功',
          message: '上架成功',
          type: 'success',
          duration: 2000
        })
      })
    },
    inactive(id) {
      statusInactive(id).then(() => {
        this.handleFilter()
        this.dialogFormVisible = false
        this.$notify({
          title: '成功',
          message: '下架成功',
          type: 'success',
          duration: 2000
        })
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
