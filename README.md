# Cornac-AB

**Cornac-AB** is an open-source solution for A/B testing with integration from the **Cornac** framework.

This tool provides you a solution to let you experiment with different recommendation models, visualize A/B test results, and analyze user interactions.

## Key Features
| User Interaction Solution | Recommendations Dashboard | Feedback Dashboard |
|:------------------------:|:------------------------:|:------------------:|
| <img src="screenshots/demo.png" alt="demo" width="250"/> | <img src="screenshots/recommendation-dashboard.png" alt="recommendations" width="250"/> | <img src="screenshots/feedback-dashboard.png" alt="feedback" width="250"/> |

- **OpenSearch Integration**: Provides robust data indexing, retrieval, and visualization.
- **Easy Experiment Setup**: Effortlessly create A/B tests and collect user feedback, leveraging Cornac’s comprehensive evaluation mechanisms.
- **Interactive Dashboards**: Analyze model behavior, user interactions, and A/B test outcomes with visually rich dashboards.

## What is Cornac?
**[Cornac](https://github.com/preferredAI/cornac)** is an open-source Python library designed for multimodal recommender systems. 

It offers a wide variety of models for collaborative filtering, content-based, explainable, and next-item or next-basket recommendation.

Cornac is endorsed by ACM RecSys for evaluating and reproducing recommendation algorithms.

## Architecture Overview

<img src="screenshots/architecture-diagram.png" alt="architecture" width="450"/>

The architecture consists of the following components:

1. **Cornac-AB Backend Server** ([Backend source code](backend)): Handles API endpoints and business logic.
2. **Cornac-AB Frontend** ([Frontend source code](frontend)): Offers a user interface for interacting with the A/B tests.
3. **Books-AB User Interaction Frontend** ([User Interaction Frontend source code](democlient)): Provides a frontend for user interactions.
4. **OpenSearch & OpenSearch Dashboards** ([Official site](https://opensearch.org)): Data indexing, search, and visualization.
5. **GoodReads 10k Dataset** ([Goodbooks-10k repository](https://github.com/zygmuntz/goodbooks-10k)): Preloaded data for demonstration purposes.

## Quick Start

To get started with Cornac-AB, you need [Docker](https://www.docker.com/products/docker-desktop/). After installing Docker, run the following command to set up the solution:
```bash
docker compose up
```
This command will start all the required components and load the GoodReads dataset into OpenSearch for A/B testing and visualization.

## Accessing the Solution

Once the containers are running, you can access the various parts of the solution via the following URLs:
- Cornac-AB Backend Server `localhost:8080`
- Cornac-AB Frontend `localhost:8081`
- Books-AB User Interaction Frontend `localhost:8082`
- OpenSearch API `localhost:9200`
- OpenSearch Dashboards `localhost:5601`

## Further Usage

Cornac-AB is a solution which showcases how A/B Testing could be done and visualized as a forward testing experiment. Feel free to further contribute, or fork the repository and extend it to your own application needs.

## Contributing

This project welcomes contributions and suggestions. Before contributing, please see our [contribution guidelines](https://cornac.readthedocs.io/en/stable/developer/index.html).

## Citation

If you use Cornac in a scientific publication, we would appreciate citations to the following papers:

<details>
  <summary><a href="https://ieeexplore.ieee.org/abstract/document/9354572">Cornac-AB: An Open-Source Recommendation Framework with Native A/B Testing Integration</a>, Ong <i>et al.</i>, In Proceedings of the ACM Web Conference 2024.</summary>

  ```
  @inproceedings{ong2024cornacab,
    title={Cornac-AB: An Open-Source Recommendation Framework with Native A/B Testing Integration},
    author={Ong, Darryl and Truong, Quoc-Tuan and Lauw, Hady W},
    journal={Proceedings of the ACM Web Conference 2024},
    pages={xx--yy},
    year={2024}
  }
  ```
</details>

<details>
  <summary><a href="http://jmlr.org/papers/v21/19-805.html">Cornac: A Comparative Framework for Multimodal Recommender Systems</a>, Salah <i>et al.</i>, Journal of Machine Learning Research, 21(95):1–5, 2020.</summary>

  ```
  @article{salah2020cornac,
    title={Cornac: A Comparative Framework for Multimodal Recommender Systems},
    author={Salah, Aghiles and Truong, Quoc-Tuan and Lauw, Hady W},
    journal={Journal of Machine Learning Research},
    volume={21},
    number={95},
    pages={1--5},
    year={2020}
  }
  ```
</details>

## License

[Apache License 2.0](LICENSE)
