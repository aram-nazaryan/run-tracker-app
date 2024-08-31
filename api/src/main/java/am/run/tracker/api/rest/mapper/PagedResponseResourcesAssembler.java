package am.run.tracker.api.rest.mapper;

import am.run.tracker.api.config.HateoasPageableHandlerMethodArgumentResolverImpl;
import am.run.tracker.core.common.datatypes.PageResponse;
import am.run.tracker.core.common.datatypes.user.PageRequest;
import org.springframework.context.annotation.Primary;
import org.springframework.core.MethodParameter;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.*;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.core.EmbeddedWrapper;
import org.springframework.hateoas.server.core.EmbeddedWrappers;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.util.UriComponentsBuilder.fromUri;

/**
 * {@link RepresentationModelAssembler} to easily convert {@link PageResponse} instances into {@link PagedModel}.
 *
 * @since 1.6
 * @author Oliver Gierke
 * @author Nick Williams
 * @author Marcel Overdijk
 */
@Component
@Primary
public class PagedResponseResourcesAssembler<T> implements RepresentationModelAssembler<PageResponse<T>, PagedModel<EntityModel<T>>> {

    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver;
    private final Optional<UriComponents> baseUri;
    private final EmbeddedWrappers wrappers = new EmbeddedWrappers(false);

    private boolean forceFirstAndLastRels = false;

    /**
     * Creates a new {@link PagedResponseResourcesAssembler} using the given {@link PageableHandlerMethodArgumentResolver} and
     * base URI. If the former is {@literal null}, a default one will be created. If the latter is {@literal null}, calls
     * to {@link #toModel(PageResponse)} will use the current request's URI to build the relevant previous and next links.
     *
     * @param resolver can be {@literal null}.
     * @param baseUri can be {@literal null}.
     */
    public PagedResponseResourcesAssembler(@Nullable HateoasPageableHandlerMethodArgumentResolverImpl resolver,
                                           @Nullable UriComponents baseUri) {

        this.pageableResolver = resolver == null ? new HateoasPageableHandlerMethodArgumentResolver() : resolver;
        this.baseUri = Optional.ofNullable(baseUri);
    }

    /**
     * Configures whether to always add {@code first} and {@code last} links to the {@link PagedModel} created. Defaults
     * to {@literal false} which means that {@code first} and {@code last} links only appear in conjunction with
     * {@code prev} and {@code next} links.
     *
     * @param forceFirstAndLastRels whether to always add {@code first} and {@code last} links to the {@link PagedModel}
     *          created.
     * @since 1.11
     */
    public void setForceFirstAndLastRels(boolean forceFirstAndLastRels) {
        this.forceFirstAndLastRels = forceFirstAndLastRels;
    }

    @Override
    public PagedModel<EntityModel<T>> toModel(PageResponse<T> entity) {
        return toModel(entity, EntityModel::of);
    }

    /**
     * Creates a new {@link PagedModel} by converting the given {@link PageResponse} into a {@link PageMetadata} instance and
     * wrapping the contained elements into {@link PagedModel} instances. Will add pagination links based on the given the
     * self link.
     *
     * @param page must not be {@literal null}.
     * @param selfLink must not be {@literal null}.
     * @return
     */
    public PagedModel<EntityModel<T>> toModel(PageResponse<T> page, Link selfLink) {
        return toModel(page, EntityModel::of, selfLink);
    }

    /**
     * Creates a new {@link PagedModel} by converting the given {@link PageResponse} into a {@link PageMetadata} instance and
     * using the given {@link PagedModel} to turn elements of the {@link PageResponse} into resources.
     *
     * @param page must not be {@literal null}.
     * @param assembler must not be {@literal null}.
     * @return
     */
    public <R extends RepresentationModel<?>> PagedModel<R> toModel(PageResponse<T> page,
                                                                    RepresentationModelAssembler<T, R> assembler) {
        return createModel(page, assembler, Optional.empty());
    }

    /**
     * Creates a new {@link PagedModel} by converting the given {@link PageResponse} into a {@link PageMetadata} instance and
     * using the given {@link PagedModel} to turn elements of the {@link PageResponse} into resources. Will add pagination links
     * based on the given the self link.
     *
     * @param page must not be {@literal null}.
     * @param assembler must not be {@literal null}.
     * @param link must not be {@literal null}.
     * @return
     */
    public <R extends RepresentationModel<?>> PagedModel<R> toModel(PageResponse<T> page,
                                                                    RepresentationModelAssembler<T, R> assembler, Link link) {

        Assert.notNull(link, "Link must not be null");

        return createModel(page, assembler, Optional.of(link));
    }

    /**
     * Creates a {@link PagedModel} with an empt collection {@link EmbeddedWrapper} for the given domain type.
     *
     * @param page must not be {@literal null}, content must be empty.
     * @param type must not be {@literal null}.
     * @return
     * @since 2.0
     */
    public PagedModel<?> toEmptyModel(PageResponse<?> page, Class<?> type) {
        return toEmptyModel(page, type, Optional.empty());
    }

    /**
     * Creates a {@link PagedModel} with an empt collection {@link EmbeddedWrapper} for the given domain type.
     *
     * @param page must not be {@literal null}, content must be empty.
     * @param type must not be {@literal null}.
     * @param link must not be {@literal null}.
     * @return
     * @since 1.11
     */
    public PagedModel<?> toEmptyModel(PageResponse<?> page, Class<?> type, Link link) {
        return toEmptyModel(page, type, Optional.of(link));
    }

    private PagedModel<?> toEmptyModel(PageResponse<?> page, Class<?> type, Optional<Link> link) {

        Assert.notNull(page, "PageResponse must not be null");
        Assert.isTrue(!CollectionUtils.isEmpty(page.content()), "PageResponse must not have any content");
        Assert.notNull(type, "Type must not be null");
        Assert.notNull(link, "Link must not be null");

        PageMetadata metadata = asPageMetadata(page);

        EmbeddedWrapper wrapper = wrappers.emptyCollectionOf(type);
        List<EmbeddedWrapper> embedded = Collections.singletonList(wrapper);

        return addPaginationLinks(PagedModel.of(embedded, metadata), page, link);
    }

    /**
     * Creates the {@link PagedModel} to be equipped with pagination links downstream.
     *
     * @param resources the original page's elements mapped into {@link RepresentationModel} instances.
     * @param metadata the calculated {@link PageMetadata}, must not be {@literal null}.
     * @param page the original page handed to the assembler, must not be {@literal null}.
     * @return must not be {@literal null}.
     */
    protected <R extends RepresentationModel<?>, S> PagedModel<R> createPagedModel(List<R> resources,
                                                                                   PageMetadata metadata, PageResponse<S> page) {

        Assert.notNull(resources, "Content resources must not be null");
        Assert.notNull(metadata, "PageMetadata must not be null");
        Assert.notNull(page, "PageResponse must not be null");

        return PagedModel.of(resources, metadata);
    }

    private <S, R extends RepresentationModel<?>> PagedModel<R> createModel(PageResponse<S> page,
                                                                            RepresentationModelAssembler<S, R> assembler, Optional<Link> link) {

        Assert.notNull(page, "PageResponse must not be null");
        Assert.notNull(assembler, "ResourceAssembler must not be null");

        List<R> resources = new ArrayList<>(page.content().size());

        for (S element : page.content()) {
            resources.add(assembler.toModel(element));
        }

        PagedModel<R> resource = createPagedModel(resources, asPageMetadata(page), page);

        return addPaginationLinks(resource, page, link);
    }

    private <R> PagedModel<R> addPaginationLinks(PagedModel<R> resources, PageResponse<?> page, Optional<Link> link) {

        UriTemplate base = getUriTemplate(link);

        boolean isNavigable = page.hasPrevious() || page.hasNext();

        if (isNavigable || forceFirstAndLastRels) {
            resources.add(createLink(base, new PageRequest(0, page.size(), page.sortProperty(), page.sortDirection()), IanaLinkRelations.FIRST));
        }

        if (page.hasPrevious()) {
            resources.add(createLink(base, new PageRequest(page.number() - 1, page.size(), page.sortProperty(), page.sortDirection()), IanaLinkRelations.PREV));
        }

        Link selfLink = link.map(Link::withSelfRel)//
                .orElseGet(() -> createLink(base, new PageRequest(page.number(), page.size(), page.sortProperty(), page.sortDirection()), IanaLinkRelations.SELF));

        resources.add(selfLink);

        if (page.hasNext()) {
            resources.add(createLink(base, new PageRequest(page.number() + 1, page.size(), page.sortProperty(), page.sortDirection()), IanaLinkRelations.NEXT));
        }

        if (isNavigable || forceFirstAndLastRels) {

            int lastIndex = page.totalPages() == 0 ? 0 : page.totalPages() - 1;

            resources
                    .add(createLink(base, new PageRequest(lastIndex, page.size(), page.sortProperty(), page.sortDirection()), IanaLinkRelations.LAST));
        }

        return resources;
    }

    /**
     * Returns a default URI string either from the one configured on assembler creatino or by looking it up from the
     * current request.
     *
     * @return
     */
    private UriTemplate getUriTemplate(Optional<Link> baseLink) {
        return UriTemplate.of(baseLink.map(Link::getHref).orElseGet(this::baseUriOrCurrentRequest));
    }

    /**
     * Creates a {@link Link} with the given {@link LinkRelation} that will be based on the given {@link UriTemplate} but
     * enriched with the values of the given {@link PageRequest} (if not {@literal null}).
     *
     * @param base must not be {@literal null}.
     * @param pageable can be {@literal null}
     * @param relation must not be {@literal null}.
     * @return
     */
    private Link createLink(UriTemplate base, PageRequest pageable, LinkRelation relation) {

        UriComponentsBuilder builder = fromUri(base.expand());
        pageableResolver.enhance(builder, getMethodParameter(), pageable);

        return Link.of(UriTemplate.of(builder.build().toString()), relation);
    }

    /**
     * Return the {@link MethodParameter} to be used to potentially qualify the paging and sorting request parameters to.
     * Default implementations returns {@literal null}, which means the parameters will not be qualified.
     *
     * @return
     * @since 1.7
     */
    @Nullable
    protected MethodParameter getMethodParameter() {
        return null;
    }

    /**
     * Creates a new {@link PageMetadata} instance from the given {@link PageResponse}.
     *
     * @param page must not be {@literal null}.
     * @return
     */
    private PageMetadata asPageMetadata(PageResponse<?> page) {

        Assert.notNull(page, "PageResponse must not be null");

        return new PageMetadata(page.size(), page.number(), page.totalElements(), page.totalPages());
    }

    private String baseUriOrCurrentRequest() {
        return baseUri.map(Object::toString).orElseGet(PagedResponseResourcesAssembler::currentRequest);
    }

    private static String currentRequest() {
        return ServletUriComponentsBuilder.fromCurrentRequest().build().toString();
    }
}
