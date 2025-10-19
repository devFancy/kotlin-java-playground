package io.dodn.commerce.core.support.auth

import io.dodn.commerce.core.domain.User
import io.dodn.commerce.core.support.error.CoreException
import io.dodn.commerce.core.support.error.ErrorType
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

class UserArgumentResolver : HandlerMethodArgumentResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return parameter.parameterType == User::class.java
    }

    override fun resolveArgument(parameter: MethodParameter, mavContainer: ModelAndViewContainer?, webRequest: NativeWebRequest, binderFactory: WebDataBinderFactory?): User {
        val request = webRequest.getNativeRequest(HttpServletRequest::class.java) ?: throw CoreException(ErrorType.INVALID_REQUEST)

        /**
         * NOTE:
         * 해당 서버는 G/W 뒷단에 Internal Network 에 존재하는 것을 가정
         * 유저 인증의 경우 G/W 단에서 처리 한 후 userId 만 보내주는 상황을 가정
         */
        val userId = request.getHeader("DODN-Commerce-User-Id") ?: throw CoreException(ErrorType.INVALID_REQUEST)
        return User(userId.toLong())
    }
}
