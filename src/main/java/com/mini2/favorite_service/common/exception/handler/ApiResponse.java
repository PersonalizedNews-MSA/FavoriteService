package com.mini2.favorite_service.common.exception.handler;

public record ApiResponse(ApiStatus status,
                          String message,
                          String errorCode,
                          Object data       )
{
    public static ApiResponse error(String message, String errorCode){
        return new ApiResponse(ApiStatus.ERROR,message,errorCode,null);
    }

}
