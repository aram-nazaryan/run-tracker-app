package am.run.tracker.api.config;

import am.run.tracker.core.common.datatypes.user.PageRequest;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class HateoasPageableHandlerMethodArgumentResolverImpl extends HateoasPageableHandlerMethodArgumentResolver {

    private final HateoasSortHandlerMethodArgumentResolverImpl sortResolver;

    public HateoasPageableHandlerMethodArgumentResolverImpl(HateoasSortHandlerMethodArgumentResolverImpl sortResolver) {
        this.sortResolver = sortResolver;
    }

    @Override
    public void enhance(UriComponentsBuilder builder, @Nullable MethodParameter parameter, Object value) {

        Assert.notNull(builder, "UriComponentsBuilder must not be null");

        if (!(value instanceof PageRequest pageRequest)) {
            return;
        }

        String pagePropertyName = getParameterNameToUse(getPageParameterName(), parameter);
        String sizePropertyName = getParameterNameToUse(getSizeParameterName(), parameter);

        int pageNumber = pageRequest.page();

        builder.replaceQueryParam(pagePropertyName, isOneIndexedParameters() ? pageNumber + 1 : pageNumber);
        builder.replaceQueryParam(sizePropertyName,
                pageRequest.size() <= getMaxPageSize() ? pageRequest.size() : getMaxPageSize());

        this.sortResolver.enhance(builder, parameter, pageRequest);
    }
}
