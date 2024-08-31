package am.run.tracker.api.config;

import am.run.tracker.core.common.datatypes.user.PageRequest;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.HateoasSortHandlerMethodArgumentResolver;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class HateoasSortHandlerMethodArgumentResolverImpl extends HateoasSortHandlerMethodArgumentResolver {

    @Override
    public void enhance(UriComponentsBuilder builder, @Nullable MethodParameter parameter, @Nullable Object value) {

        if (!(value instanceof PageRequest pageRequest)) {
            return;
        }

        builder.replaceQueryParam("sortProperty", pageRequest.sortProperty());
        builder.replaceQueryParam("sortDirection", pageRequest.sortDirection());
    }
}
