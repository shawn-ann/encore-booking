<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select
        v-model="listQuery.concertId"
        filterable
        placeholder="请选择"
        class="filter-item"
      >
        <el-option
          v-for="item in concertDropdownOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-select
        v-model="listQuery.agentId"
        filterable
        placeholder="请选择"
        class="filter-item"
      >
        <el-option
          v-for="item in agentDropdownOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
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

      <el-table-column align="center" label="单价">
        <template slot-scope="scope">
          <span>{{ scope.row.price }}元</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="可售数量">
        <template slot-scope="scope">
          <span>{{ scope.row.totalQuantity }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="剩余数量" width="180">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == 'INACTIVE'"
            type="primary"
            size="mini"
            icon="el-icon-plus"
            @click="handleQuantityUpdate(scope.row.id,'ADD')"
          />
          <el-tag>
            <span>{{ scope.row.remainingQuantity }} </span>
          </el-tag>
          <el-button
            v-if="scope.row.status == 'INACTIVE'"
            type="primary"
            size="mini"
            icon="el-icon-minus"
            @click="handleQuantityUpdate(scope.row.id,'MINUS')"
          />
        </template>
      </el-table-column>

      <el-table-column align="center" label="状态">
        <template slot-scope="scope">
          <el-tag>
            <span>{{ scope.row.status | statusFilter }}</span>
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="220">
        <template slot-scope="scope">
          <el-button
            v-if="scope.row.status == 'INACTIVE'"
            type="primary"
            size="mini"
            @click="handleUpdate(scope.row)"
          >
            编辑
          </el-button>
          <el-button v-if="scope.row.status == 'ACTIVE'" type="info" size="mini" @click="inactive(scope.row.id)">
            禁用
          </el-button>
          <el-button
            v-if="scope.row.status == 'INACTIVE'"
            type="success"
            size="mini"
            @click="active(scope.row.id)"
          >
            启用
          </el-button>
          <el-button
            v-if="scope.row.status == 'INACTIVE'"
            size="mini"
            type="danger"
            @click="handleDelete(scope.row.id,$index)"
          >
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
        style="width: 400px; margin-left:50px;"
      >

        <el-form-item v-if="dialogStatus == 'create'" label="演唱会" prop="concertId" label-width="120px">
          <el-select
            v-model="temp.concertId"
            filterable
            placeholder="请选择"
            class="filter-item"
            @change="changeConcert()"
          >
            <el-option
              v-for="item in concertDropdownOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item v-if="dialogStatus == 'create'" label="场次-票型" prop="inventoryId" label-width="120px">
          <el-select
            v-model="temp.inventoryId"
            filterable
            placeholder="请选择"
            class="filter-item"
          >
            <el-option
              v-for="item in inventoryDropdownOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item v-if="dialogStatus == 'create'" label="代理商" prop="agentId" label-width="120px">
          <el-select
            v-model="temp.agentId"
            filterable
            placeholder="请选择"
            class="filter-item"
          >
            <el-option
              v-for="item in agentDropdownOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="单价(元)" prop="price" label-width="120px">
          <el-input v-model.number="temp.price" />
        </el-form-item>
        <el-form-item v-if="dialogStatus == 'create'" label="数量" prop="quantity" label-width="120px">
          <el-input v-model.number="temp.quantity" />
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
          <el-input v-model.number="stock.quantity" />
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
import { fetchList, createItem, updateItem, deleteItem, statusActive, statusInactive, updateQuantity } from '@/api/quota'
import { fetchDropdown as fetchConcertDropdown } from '@/api/concert'
import { fetchDropdown as fetchAgentDropdown } from '@/api/agent'
import { fetchDropdown as fetchInventoryDropdown } from '@/api/inventory'
import Pagination from '@/components/Pagination'

export default {
  name: 'QuotaList',
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        ACTIVE: '启用',
        INACTIVE: '禁用'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: false,
      concertDropdownOptions: [],
      agentDropdownOptions: [],
      inventoryDropdownOptions: [],
      listQuery: {
        page: 1,
        limit: 20,
        concertId: '',
        agentId: ''
      },
      temp: {
        id: undefined,
        concertId: undefined,
        agentId: undefined,
        inventoryId: undefined,
        quantity: undefined,
        price: undefined
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
        create: '添加',
        ADD: '添加库存',
        MINUS: '扣减库存'
      },
      rules: {
        concertId: [{ required: true, message: '请选择演唱会' }],
        agentId: [{ required: true, message: '请选择代理商' }],
        inventoryId: [{ required: true, message: '请选择场次票型' }],
        quantity: [
          { required: true, message: '请输入数量' },
          { type: 'number', message: '数量必须为数字值' }],
        price: [
          { required: true, message: '请输入单价' },
          { type: 'number', message: '单价必须为数字值' }]
      }
    }
  },
  created() {
    this.initDropdown()
    this.getList()
  },
  methods: {
    initDropdown() {
      fetchConcertDropdown().then(response => {
        this.concertDropdownOptions = response.data
      })
      fetchAgentDropdown().then(response => {
        this.agentDropdownOptions = response.data
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
        agentId: undefined,
        inventoryId: undefined,
        quantity: undefined,
        price: undefined
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
    changeConcert() {
      const concertId = this.temp.concertId
      if (!concertId) {
        return
      }
      this.temp.inventoryId = undefined
      fetchInventoryDropdown(concertId).then(response => {
        this.inventoryDropdownOptions = response.data
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
        if (valid) {
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
    handleQuantityUpdate(id, opt) {
      this.stock = {
        id: id,
        operation: opt,
        quantity: undefined
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
