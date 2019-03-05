#!/bin/sh -x

image_tag="have_to_create_ecr"


docker_ensure_volume() {
  docker volume inspect $1 >/dev/null 2>&1 || docker volume create $1 >/dev/null 2>&1
}

runs_inside_gocd() {
  test -n "${GO_JOB_NAME}"
}

docker_run(){
    docker run --rm ${DOCKER_ARGS} ./gradlew "$@" --stacktrace
}

run() {
   docker_ensure_volume gradle-cache
   DOCKER_ARGS="${DOCKER_ARGS} --user root -w /home/gradle/  -v gradle-cache:/home/gradle/.gradle -v $PWD:/home/gradle gradle:5.0.0-jdk11" docker_run "$@"
   local exit=$?
   return $exit
}

goal_pushEcr(){
    aws ecr get-login --no-include-email --region ap-southeast-1 | bash
    docker push ${image_tag}
}

goal_containerize() {
    run build
    docker build --pull \
               --label org.label-schema.vcs-ref=$(git rev-parse HEAD) \
               -f Dockerfile.production \
               -t oauth_simple:latest \
               -t ${image_tag} \
               .
}
goal_run(){
    docker run --rm -p8081:8081 --name oauth oauth_simple:latest
}

goal_build() {
    run clean build
}

goal_test() {
    run clean test
}

goal_lint() {
    run clean ktlint
}


if type -t "goal_$1" &>/dev/null; then
  goal_$1 ${@:-2}
else
    echo "usage: $0 <goal>
    goal:
        build               -- build an executable jar file
        test                -- run  tests
        lint                -- lint  code
        containerize        -- build production ready docker
        pushEcr             -- Push to ECR
        run                 -- run containerized application"

  exit 1
fi
