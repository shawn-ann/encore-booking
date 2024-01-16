<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.concertName"
        placeholder="演唱会名称"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-input
        v-model="listQuery.agentMobile"
        placeholder="代理商手机号"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-date-picker
        v-model="listQuery.dateRange"
        type="daterange"
        style="width: 300px;"
        class="filter-item"
        range-separator="至"
        start-placeholder="下单开始日期"
        end-placeholder="下单结束日期"
        format="yyyy-MM-dd"
        value-format="yyyy-MM-dd"
      />
      <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        查询
      </el-button>
    </div>
    <el-table v-loading="listLoading" :data="list" border fit highlight-current-row style="width: 100%">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="table-expand">
            <el-row>
              <el-form-item label="购票人">
                <template>
                  <el-table
                    :show-header="false"
                    :data="props.row.buyerList"
                    style="width: 100%"
                  >
                    <el-table-column
                      prop="name"
                      label="姓名"
                      width="180"
                    />
                    <el-table-column
                      prop="idNumber"
                      label="身份证号"
                      width="180"
                    />
                    <el-table-column
                      prop="mobile"
                      label="手机号"
                      width="180"
                    />
                  </el-table>
                </template>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="操作历史">
                <template>
                  <el-table
                    :show-header="false"
                    :data="props.row.operationList"
                    style="width: 100%"
                  >
                    <el-table-column
                      prop="createDate"
                      label="日期"
                      width="180"
                    />
                    <el-table-column
                      prop="statusName"
                      label="状态"
                      width="180"
                    />
                  </el-table>
                </template>
              </el-form-item>
            </el-row>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" label="订单号">
        <template slot-scope="scope">
          <span>{{ scope.row.orderNumber }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="演唱会">
        <template slot-scope="scope">
          <span>{{ scope.row.concertName }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="票档">
        <template slot-scope="scope">
          <span>{{ scope.row.sessionName }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="场次">
        <template slot-scope="scope">
          <span>{{ scope.row.ticketCategoryName }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态">
        <template slot-scope="scope">
          <span>{{ scope.row.statusName }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="数量">
        <template slot-scope="scope">
          <span>{{ scope.row.buyCount }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="合计">
        <template slot-scope="scope">
          <span>{{ scope.row.totalFee/100 }}</span>
        </template>
      </el-table-column>

      <!--      <el-table-column align="center" label="操作" width="240">-->
      <!--        <template slot-scope="scope">-->
      <!--          <el-button type="primary" size="mini" @click="handleMore(scope.row)">-->
      <!--            更多-->
      <!--          </el-button>-->
      <!--          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id,$index)">-->
      <!--            删除-->
      <!--          </el-button>-->
      <!--        </template>-->
      <!--      </el-table-column>-->
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

  </div>
</template>

<script>
import { fetchList, fetchDetail } from '@/api/booking-order'
import Pagination from '@/components/Pagination'

export default {
  name: 'ConcertList',
  components: { Pagination },
  data() {
    return {
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        concertName: '',
        agentMobile: '',
        dateRange: ['', '']
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
    handleMore(row) {
      fetchDetail(row.id).then(res => {
        debugger
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
