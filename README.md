# Cornac-AB

Cornac-AB is an A/B Testing solution that integrates OpenSearch and Cornac.
It allows you to easily run A/B tests on your OpenSearch cluster using the power of Cornac.

## What is Cornac?
* [Cornac](https://github.com/preferredAI/cornac) is an open-source Python library for multimodal recommender systems. 
* A rich collection of models for collaborative filtering, content-based, explainable and next-item, next-basket recommendation.
* Endorsed by ACM RecSys for evaluation and reproducibility of recommendation algorithms.

## Features

- Seamless integration with OpenSearch
- Simple setup and configuration
- Flexible A/B testing capabilities
- Powerful analytics and reporting

## Architecture

This solution consists of the following:

1. [Cornac-AB Backend Server](backend)
2. [Cornac-AB Frontend](frontend)
3. [Books-AB User Interaction Frontend](democlient)
4. OpenSearch & OpenSearch Dashboards
5. [GoodReads 10k dataset preloaded into OpenSearch](https://github.com/zygmuntz/goodbooks-10k)

## Scope of Repository

This solution spins up Cornac-AB backend and frontend solution. In addition, this also bootstraps and adds sample data from GoodReads 10k dataset into OpenSearch and OpenSearch Dashboards.

## Running the Solution

Running this solution would require [Docker](https://www.docker.com/products/docker-desktop/). After Docker is running, run the following command:
```
docker compose up
```

This would bring spin up multiple containers as mentioned in the architecture above, including insertion of the GoodReads dataset into OpenSearch and sample dashboards.

## Usage

Upon successful container creation, access the solution through the following URLS:
- Cornac-AB Backend Server `localhost:8080`
- Cornac-AB Frontend `localhost:8081`
- Books-AB User Interaction Frontend `localhost:8082`
- OpenSearch API `localhost:9200`
- OpenSearch Dashboards `localhost:5601`

## Further Usage

Cornac-AB is a solution which showcases how A/B Testing could be done and visualized as a forward testing experiment. Feel free to further contribute, or fork the repository and extend it to your own application needs.

## Contributing

We welcome contributions from the community! Please feel free to file issues and create pull requests to contribute to this repository.

## License

[Apache License 2.0](LICENSE)
