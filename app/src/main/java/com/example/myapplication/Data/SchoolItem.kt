package com.example.myapplication.Data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
data class SchoolItem(
    val dbn: String?= null,
    val school_name: String?=null,
    val school_sports: String?=null,
    val total_students: String?=null,
    val website: String?=null
){

    override fun equals(other: Any?): Boolean {
        if(other is SchoolItem) {
            return this.dbn == other.dbn
        }
        return false
    }

    override fun hashCode(): Int {
        return Objects.hash(this)
    }
}