package cz.vsb.pjp.bed0152.project.util

interface VariablesHolder {

    fun getVariable(varName: String): Type

    fun getVariableOrNull(varName: String): Type?
}