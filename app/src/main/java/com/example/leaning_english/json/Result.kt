package com.example.leaning_english.json

class Result<T>(// 状态码 0：成功 1：失败 2：未登录 3：匹配成功 4：匹配失败 5：单词选择
    private val code: Int, // 提示信息
    private val message: String, // 数据
    private var data: T
) {
    companion object {
        fun <E> success(data: E): Result<E> {
            return Result(0, "success", data)
        }

        // 快速返回操作成功响应
        fun success(): Result<*> {
            return Result<Any?>(0, "success", null)
        }

        fun error(code: Int, message: String): Result<*> {
            return Result<Any?>(code, message, null)
        }
    }
}