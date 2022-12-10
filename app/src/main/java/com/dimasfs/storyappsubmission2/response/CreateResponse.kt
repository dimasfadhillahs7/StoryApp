package com.dimasfs.storyappsubmission2.response

import com.google.gson.annotations.SerializedName

class CreateResponse (
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String

    )